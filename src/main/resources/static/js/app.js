document.addEventListener('DOMContentLoaded', () => {
  const searchInput = document.getElementById('searchInput');
  const topicFilter = document.getElementById('topicFilter');
  const difficultyFilter = document.getElementById('difficultyFilter');
  const clearFiltersBtn = document.getElementById('clearFiltersBtn');
  const problemsGrid = document.getElementById('problemsGrid');
  const problemCountText = document.getElementById('problemCountText');
  const statTotalProblems = document.getElementById('stat-total-problems');
  const statTotalTopics = document.getElementById('stat-total-topics');
  const statTotalHints = document.getElementById('stat-total-hints');

  let debounceTimer = null;

  // Load Topics into dropdown
  async function loadTopics() {
    try {
      const res = await fetch('/api/topics');
      if (!res.ok) throw new Error('Failed to load topics');
      const topics = await res.json();
      
      topics.forEach(t => {
        const opt = document.createElement('option');
        opt.value = t.slug;
        opt.textContent = `${t.name} (${t.problemCount})`;
        topicFilter.appendChild(opt);
      });

      if (statTotalTopics) {
        statTotalTopics.textContent = topics.length;
      }
    } catch (err) {
      console.error('Error fetching topics:', err);
    }
  }

  // Fetch and render problems based on active filters
  async function fetchProblems() {
    const search = searchInput.value.trim();
    const topic = topicFilter.value;
    const difficulty = difficultyFilter.value;

    const params = new URLSearchParams();
    if (search) params.append('search', search);
    if (topic) params.append('topic', topic);
    if (difficulty) params.append('difficulty', difficulty);

    try {
      problemsGrid.innerHTML = `
        <div style="grid-column: 1 / -1; text-align: center; padding: 3rem; color: var(--text-dim);">
          Loading problems...
        </div>
      `;

      const res = await fetch(`/api/problems?${params.toString()}`);
      if (!res.ok) throw new Error('Failed to fetch problems');
      const problems = await res.json();

      renderProblems(problems);
    } catch (err) {
      console.error('Error fetching problems:', err);
      problemsGrid.innerHTML = `
        <div style="grid-column: 1 / -1; text-align: center; padding: 3rem; color: var(--hard-color);">
          Unable to connect to the backend server. Ensure the Spring Boot application is running.
        </div>
      `;
    }
  }

  function renderProblems(problems) {
    if (!problems || problems.length === 0) {
      problemsGrid.innerHTML = `
        <div style="grid-column: 1 / -1; text-align: center; padding: 3rem; color: var(--text-dim); background: var(--bg-surface); border-radius: var(--radius-md);">
          <h3>No matching problems found</h3>
          <p style="margin-top: 0.5rem; color: var(--text-muted);">Try clearing search filters or selecting another category.</p>
        </div>
      `;
      problemCountText.textContent = 'Showing 0 problems';
      return;
    }

    problemCountText.textContent = `Showing ${problems.length} of ${problems.length} problem${problems.length === 1 ? '' : 's'}`;

    if (statTotalProblems && !searchInput.value && !topicFilter.value && !difficultyFilter.value) {
      statTotalProblems.textContent = problems.length;
      const totalHints = problems.reduce((acc, p) => acc + (p.hintCount || 4), 0);
      if (statTotalHints) statTotalHints.textContent = totalHints;
    }

    problemsGrid.innerHTML = problems.map(p => {
      const diffClass = `difficulty-${p.difficulty.toLowerCase()}`;
      return `
        <div class="problem-card">
          <div>
            <div class="problem-card-header">
              <h3 class="problem-card-title">${escapeHtml(p.title)}</h3>
              <span class="difficulty-badge ${diffClass}">${p.difficulty}</span>
            </div>
            <div class="problem-card-topic">
              <span>🏷️</span> ${escapeHtml(p.topicName || 'DSA Topic')}
            </div>
            <div class="hints-indicator">
              <div class="hints-dots">
                <span class="hint-dot active"></span>
                <span class="hint-dot active"></span>
                <span class="hint-dot active"></span>
                <span class="hint-dot active"></span>
              </div>
              <span>${p.hintCount || 4} Progressive Hints Available</span>
            </div>
          </div>
          <div>
            <a href="/problem.html?id=${p.id}" class="btn-primary" style="width: 100%;">
              Solve Problem <span>➔</span>
            </a>
          </div>
        </div>
      `;
    }).join('');
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
  searchInput.addEventListener('input', () => {
    clearTimeout(debounceTimer);
    debounceTimer = setTimeout(fetchProblems, 250);
  });

  topicFilter.addEventListener('change', fetchProblems);
  difficultyFilter.addEventListener('change', fetchProblems);

  clearFiltersBtn.addEventListener('click', () => {
    searchInput.value = '';
    topicFilter.value = '';
    difficultyFilter.value = '';
    fetchProblems();
  });

  // Initial load
  loadTopics();
  fetchProblems();
});
