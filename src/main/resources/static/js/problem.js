document.addEventListener('DOMContentLoaded', () => {
  // Elements
  const loadingState = document.getElementById('loadingState');
  const workspaceContent = document.getElementById('workspaceContent');
  const problemTitle = document.getElementById('problemTitle');
  const breadcrumbTitle = document.getElementById('breadcrumbTitle');
  const breadcrumbTopic = document.getElementById('breadcrumbTopic');
  const problemTopicBadge = document.getElementById('problemTopicBadge');
  const problemDifficultyBadge = document.getElementById('problemDifficultyBadge');
  const problemDescription = document.getElementById('problemDescription');
  const inputFormatText = document.getElementById('inputFormatText');
  const outputFormatText = document.getElementById('outputFormatText');
  const sampleInputText = document.getElementById('sampleInputText');
  const sampleOutputText = document.getElementById('sampleOutputText');
  const sampleExplanationText = document.getElementById('sampleExplanationText');
  const constraintsText = document.getElementById('constraintsText');

  // Progressive Hint Engine elements
  const hintLevelBadge = document.getElementById('hintLevelBadge');
  const stepperProgressFill = document.getElementById('stepperProgressFill');
  const showNextHintBtn = document.getElementById('showNextHintBtn');
  const resetHintsBtn = document.getElementById('resetHintsBtn');
  const hintStatusMsg = document.getElementById('hintStatusMsg');
  const unlockedHintsStack = document.getElementById('unlockedHintsStack');

  // Code solution & language elements
  const languageTabs = document.getElementById('languageTabs');
  const codeEditor = document.getElementById('codeEditor');
  const resetCodeBtn = document.getElementById('resetCodeBtn');
  const submitCodeBtn = document.getElementById('submitCodeBtn');
  const submissionResultBanner = document.getElementById('submissionResultBanner');
  const resultStatusText = document.getElementById('resultStatusText');
  const resultMetricsText = document.getElementById('resultMetricsText');
  const resultFeedbackText = document.getElementById('resultFeedbackText');

  // State
  let currentProblem = null;
  let currentLanguage = 'java';
  let languageTemplates = {};
  let currentEditedCode = {};

  // Session ID for backend progressive hint engine state
  let sessionId = localStorage.getItem('dsa_user_session_id');
  if (!sessionId) {
    sessionId = 'session_' + Math.random().toString(36).substring(2, 12);
    localStorage.setItem('dsa_user_session_id', sessionId);
  }

  // Parse problem ID or slug from URL query
  const urlParams = new URLSearchParams(window.location.search);
  const problemIdParam = urlParams.get('id') || '1';
  const problemSlugParam = urlParams.get('slug');

  // Fetch problem details
  async function loadProblem() {
    try {
      const endpoint = problemSlugParam
        ? `/api/problems/slug/${problemSlugParam}`
        : `/api/problems/${problemIdParam}`;

      const res = await fetch(endpoint);
      if (!res.ok) throw new Error('Problem not found');
      currentProblem = await res.json();

      renderProblemDetails(currentProblem);
      setupLanguageTemplates(currentProblem);
      await loadHintProgression();

      loadingState.style.display = 'none';
      workspaceContent.style.display = 'block';
    } catch (err) {
      console.error('Error loading problem:', err);
      loadingState.innerHTML = `
        <h2 style="color: var(--hard-color);">Failed to load problem</h2>
        <p style="margin-top: 0.5rem; color: var(--text-muted);">The requested problem could not be found or the server is unavailable.</p>
        <a href="/index.html" class="btn-primary" style="margin-top: 1rem;">Return to Problem Catalog</a>
      `;
    }
  }

  function renderProblemDetails(p) {
    document.title = `${p.title} | DSA Logic Building Platform`;
    problemTitle.textContent = p.title;
    breadcrumbTitle.textContent = p.title;
    breadcrumbTopic.textContent = p.topic ? p.topic.name : 'DSA';
    problemTopicBadge.textContent = p.topic ? p.topic.name : 'DSA Topic';

    problemDifficultyBadge.textContent = p.difficulty;
    problemDifficultyBadge.className = `difficulty-badge difficulty-${p.difficulty.toLowerCase()}`;

    problemDescription.textContent = p.description;
    inputFormatText.textContent = p.inputFormat || 'Not specified';
    outputFormatText.textContent = p.outputFormat || 'Not specified';
    sampleInputText.textContent = p.sampleInput || '';
    sampleOutputText.textContent = p.sampleOutput || '';
    sampleExplanationText.textContent = p.sampleExplanation || '';
    constraintsText.textContent = p.constraints || 'Standard competitive programming constraints apply.';
  }

  function setupLanguageTemplates(p) {
    languageTemplates = {};
    if (p.languageOptions && p.languageOptions.length > 0) {
      p.languageOptions.forEach(opt => {
        languageTemplates[opt.languageKey.toLowerCase()] = opt.starterTemplate;
      });
    }

    // Default template fallbacks if not provided
    if (!languageTemplates.java) languageTemplates.java = "// Java Solution\nclass Solution {\n    // Implement logic here\n}";
    if (!languageTemplates.cpp) languageTemplates.cpp = "// C++ Solution\n#include <vector>\nusing namespace std;\n\nclass Solution {\npublic:\n    // Implement logic here\n};";
    if (!languageTemplates.python) languageTemplates.python = "# Python Solution\nclass Solution:\n    # Implement logic here\n    pass";

    setLanguage(currentLanguage);
  }

  function setLanguage(langKey) {
    currentLanguage = langKey;

    // Update active tab UI
    const tabs = languageTabs.querySelectorAll('.lang-tab');
    tabs.forEach(tab => {
      if (tab.getAttribute('data-lang') === langKey) {
        tab.classList.add('active');
      } else {
        tab.classList.remove('active');
      }
    });

    // Populate editor
    if (currentEditedCode[langKey]) {
      codeEditor.value = currentEditedCode[langKey];
    } else {
      codeEditor.value = languageTemplates[langKey] || '';
    }
  }

  // Progressive Hint Engine Logic
  async function loadHintProgression() {
    try {
      const res = await fetch(`/api/problems/${currentProblem.id}/hints/progress?sessionId=${encodeURIComponent(sessionId)}`);
      if (!res.ok) throw new Error('Failed to load hint progress');
      const progress = await res.json();
      updateHintUI(progress);
    } catch (err) {
      console.error('Error fetching hint progress:', err);
    }
  }

  function updateHintUI(progress) {
    const level = progress.currentUnlockedLevel;
    const max = progress.maxLevel || 4;

    hintLevelBadge.textContent = `Level ${level} of ${max}`;

    // Update Stepper Progress Bar
    const fillPercent = (level / max) * 100;
    stepperProgressFill.style.width = `${fillPercent}%`;

    // Update Step Nodes
    for (let i = 1; i <= 4; i++) {
      const node = document.getElementById(`stepNode${i}`);
      if (!node) continue;
      node.classList.remove('unlocked', 'current');
      if (i <= level) {
        node.classList.add('unlocked');
        if (i === level) {
          node.classList.add('current');
        }
      }
    }

    // Update Buttons & Status Text
    hintStatusMsg.textContent = progress.statusMessage || '';

    if (level >= max) {
      showNextHintBtn.disabled = true;
      showNextHintBtn.innerHTML = '<span>✓ All Hints Unlocked</span>';
    } else {
      showNextHintBtn.disabled = false;
      const nextLvl = level + 1;
      const labels = ['', 'Level 1: Conceptual Nudge', 'Level 2: Relevant DSA Concept', 'Level 3: Algorithmic Logic', 'Level 4: Optimal Complexity'];
      showNextHintBtn.innerHTML = `<span>⚡ Show Next Hint (${labels[nextLvl] || 'Level ' + nextLvl})</span>`;
    }

    // Render Unlocked Hints Stack (strictly from backend response)
    renderUnlockedHints(progress.unlockedHints || []);
  }

  function renderUnlockedHints(hints) {
    if (!hints || hints.length === 0) {
      unlockedHintsStack.innerHTML = `
        <div style="background: rgba(15, 23, 42, 0.4); border: 1px dashed var(--border-color); border-radius: var(--radius-sm); padding: 1.25rem; text-align: center; color: var(--text-dim); font-size: 0.9rem;">
          No conceptual hints unlocked yet. Click <strong>"Show Next Hint"</strong> to reveal Level 1 guidance without giving away the full solution.
        </div>
      `;
      return;
    }

    unlockedHintsStack.innerHTML = hints.map(h => {
      let complexityHtml = '';
      if (h.level === 4 && (h.timeComplexity || h.spaceComplexity)) {
        complexityHtml = `
          <div class="complexity-badges">
            <span class="complexity-pill">⏱ Time: ${escapeHtml(h.timeComplexity || 'O(n)')}</span>
            <span class="complexity-pill">💾 Space: ${escapeHtml(h.spaceComplexity || 'O(1)')}</span>
          </div>
        `;
      }

      return `
        <div class="hint-card level-${h.level}">
          <div class="hint-card-title">
            <span>${escapeHtml(h.title)}</span>
            <span style="font-size: 0.75rem; color: var(--text-dim); font-weight: normal;">Unlocked</span>
          </div>
          <div class="hint-card-body">${escapeHtml(h.content)}</div>
          ${complexityHtml}
        </div>
      `;
    }).join('');
  }

  async function unlockNextHint() {
    showNextHintBtn.disabled = true;
    showNextHintBtn.innerHTML = '<span>Unlocking...</span>';

    try {
      const res = await fetch(`/api/problems/${currentProblem.id}/hints/unlock-next?sessionId=${encodeURIComponent(sessionId)}`, {
        method: 'POST'
      });

      if (!res.ok) throw new Error('Failed to unlock next hint');
      const progress = await res.json();
      updateHintUI(progress);
    } catch (err) {
      console.error('Error unlocking next hint:', err);
      showNextHintBtn.disabled = false;
    }
  }

  async function resetHints() {
    if (!confirm('Are you sure you want to reset all progressive hints for this problem?')) return;

    try {
      const res = await fetch(`/api/problems/${currentProblem.id}/hints/reset?sessionId=${encodeURIComponent(sessionId)}`, {
        method: 'POST'
      });

      if (!res.ok) throw new Error('Failed to reset hints');
      const progress = await res.json();
      updateHintUI(progress);
    } catch (err) {
      console.error('Error resetting hints:', err);
    }
  }

  // Code Submission
  async function submitSolution() {
    const code = codeEditor.value;
    submitCodeBtn.disabled = true;
    submitCodeBtn.innerHTML = '<span>Evaluating Solution...</span>';
    submissionResultBanner.style.display = 'none';

    try {
      const payload = {
        problemId: currentProblem.id,
        language: currentLanguage,
        sourceCode: code,
        sessionId: sessionId
      };

      const res = await fetch('/api/submissions', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(payload)
      });

      if (!res.ok) throw new Error('Submission evaluation failed');
      const data = await res.json();

      displaySubmissionResult(data);
    } catch (err) {
      console.error('Error submitting code:', err);
      submissionResultBanner.className = 'submission-result failed';
      resultStatusText.textContent = 'SUBMISSION ERROR';
      resultMetricsText.textContent = '';
      resultFeedbackText.textContent = 'Failed to communicate with submission engine.';
      submissionResultBanner.style.display = 'block';
    } finally {
      submitCodeBtn.disabled = false;
      submitCodeBtn.innerHTML = '<span>▶ Run &amp; Submit Solution</span>';
    }
  }

  function displaySubmissionResult(sub) {
    const isAccepted = sub.status === 'ACCEPTED';
    submissionResultBanner.className = `submission-result ${isAccepted ? 'accepted' : 'failed'}`;
    resultStatusText.textContent = sub.status;
    resultMetricsText.textContent = `Runtime: ${sub.executionTimeMs} ms | Memory: ${(sub.memoryKb / 1024).toFixed(1)} MB | Language: ${sub.language.toUpperCase()}`;
    resultFeedbackText.textContent = sub.feedback;
    submissionResultBanner.style.display = 'block';
    submissionResultBanner.scrollIntoView({ behavior: 'smooth', block: 'nearest' });
  }

  function escapeHtml(str) {
    if (!str) return '';
    return str.replace(/[&<>'"]/g, 
      tag => ({
        '&': '&amp;',
        '<': '&lt;',
        '>': '&gt;',
        "'": '&#39;',
        '"': '&quot;'
      }[tag] || tag)
    );
  }

  // Event Listeners
  languageTabs.addEventListener('click', (e) => {
    const btn = e.target.closest('.lang-tab');
    if (!btn) return;
    const lang = btn.getAttribute('data-lang');
    if (lang && lang !== currentLanguage) {
      currentEditedCode[currentLanguage] = codeEditor.value;
      setLanguage(lang);
    }
  });

  codeEditor.addEventListener('input', () => {
    currentEditedCode[currentLanguage] = codeEditor.value;
  });

  resetCodeBtn.addEventListener('click', () => {
    if (confirm(`Reset ${currentLanguage.toUpperCase()} code back to starter template?`)) {
      codeEditor.value = languageTemplates[currentLanguage] || '';
      currentEditedCode[currentLanguage] = codeEditor.value;
    }
  });

  showNextHintBtn.addEventListener('click', unlockNextHint);
  resetHintsBtn.addEventListener('click', resetHints);
  submitCodeBtn.addEventListener('click', submitSolution);

  // Initialize
  loadProblem();
});
