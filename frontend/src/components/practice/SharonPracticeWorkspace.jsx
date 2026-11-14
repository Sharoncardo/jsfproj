
import React, { useEffect, useState } from "react";
import "../../styles/sharon-practice.css";
import { problemsApi } from "../../api/problemsApi";
import { hintsApi } from "../../api/hintsApi";
import { submissionsApi } from "../../api/submissionsApi";

const LEVELS = ["Nudge", "Concept", "Algorithm", "Optimal & O(N)"];

export default function SharonPracticeWorkspace({ onProblemChange }) {
  const [problems, setProblems] = useState([]);
  const [problemId, setProblemId] = useState("");
  const [problem, setProblem] = useState(null);
  const [hintData, setHintData] = useState(null);
  const [language, setLanguage] = useState("java");
  const [code, setCode] = useState("");
  const [edited, setEdited] = useState({});
  const [loading, setLoading] = useState(true);
  const [busy, setBusy] = useState(false);
  const [result, setResult] = useState(null);
  const [error, setError] = useState("");

  useEffect(() => {
    problemsApi.getProblems()
      .then((res) => {
        const data = res.data ?? [];
        setProblems(data);
        if (data.length) setProblemId(String(data[0].id));
      })
      .catch(() => setError("Could not load problems."))
      .finally(() => setLoading(false));
  }, []);

  useEffect(() => {
    if (!problemId) return;

    let active = true;
    setProblem(null);
    setHintData(null);
    setResult(null);
    setEdited({});
    setLanguage("java");

    async function load() {
      try {
        const [pRes, hRes] = await Promise.all([
          problemsApi.getProblemById(problemId),
          hintsApi.getHints(problemId),
        ]);

        if (!active) return;

        const p = pRes.data;
        const h = hRes.data;

        setProblem(p);
        onProblemChange?.(p);
        setHintData(h);

        const templates = {};
        (p.languageOptions || []).forEach((item) => {
          templates[item.languageKey.toLowerCase()] =
            item.starterTemplate || "";
        });

        setEdited(templates);
        setCode(templates.java || "");
      } catch {
        if (active) setError("Could not load this problem.");
      }
    }

    load();
    return () => {
      active = false;
    };
  }, [problemId, onProblemChange]);

  function changeLanguage(next) {
    setEdited((old) => ({ ...old, [language]: code }));
    setLanguage(next);
    setCode(edited[next] || problem?.languageOptions?.find(
      (x) => x.languageKey.toLowerCase() === next
    )?.starterTemplate || "");
  }

  async function showNextHint() {
    if (busy || !hintData?.hasMoreHints) return;
    setBusy(true);
    try {
      const res = await hintsApi.unlockNextHint(problemId);
      setHintData(res.data);
    } catch {
      setError("Unable to unlock the next hint.");
    } finally {
      setBusy(false);
    }
  }

  async function handleSubmit() {
    setBusy(true);
    setResult(null);
    try {
      const res = await submissionsApi.submitSolution({
        problemId: Number(problemId),
        language,
        sourceCode: code,
      });
      setResult(res.data);
    } catch {
      setError("Submission failed. Please try again.");
    } finally {
      setBusy(false);
    }
  }

  if (loading) return <p>Loading problems...</p>;

  const level = hintData?.unlockedLevel || 0;
  const hints = hintData?.hints?.filter((h) => h.unlocked) || [];

  return (
    <div className="sharon-practice-workspace">
      <div className="practice-problem-select">
        <label>Select Problem: </label>
        <select value={problemId} onChange={(e) => setProblemId(e.target.value)}>
          {problems.map((p) => (
            <option key={p.id} value={p.id}>
              {p.title} — {p.difficulty}
            </option>
          ))}
        </select>
      </div>

      {error && <p role="alert">{error}</p>}

      {problem && (
        <>
          <div className="practice-breadcrumb">
            Problems / {problem.topicName || "DSA"} / {problem.title}
            <span className={`difficulty-badge difficulty-${problem.difficulty?.toLowerCase()}`}>
              {problem.difficulty}
            </span>
          </div>

          <div className="solve-workspace">
            <section className="left-column">
              <div className="panel-card">
                <div className="panel-card-header">
                  <h1 className="panel-title">{problem.title}</h1>
                  <span>{problem.topicName}</span>
                </div>

                <div className="problem-desc-content">{problem.description}</div>

                <div className="format-block">
                  <div className="format-title">Input Format</div>
                  <div className="format-code">{problem.inputFormat || "Not specified"}</div>
                </div>

                <div className="format-block">
                  <div className="format-title">Output Format</div>
                  <div className="format-code">{problem.outputFormat || "Not specified"}</div>
                </div>

                <h3>Sample Example</h3>
                <div className="format-block">
                  <strong>Input:</strong>
                  <div className="format-code">{problem.sampleInput}</div>
                  <strong>Output:</strong>
                  <div className="format-code">{problem.sampleOutput}</div>
                  <strong>Explanation:</strong>
                  <p>{problem.sampleExplanation}</p>
                </div>

                <h3>Constraints</h3>
                <div className="constraints-list">{problem.constraints}</div>
              </div>
            </section>

            <section className="right-column">
              <div className="hint-engine-card">
                <div className="engine-header">
                  <div>
                    <h3>💡 Progressive Conceptual Hint Engine</h3>
                    <p>Guided algorithmic logic revelation</p>
                  </div>
                  <span className="engine-badge">
                    Level {level} of {hintData?.totalHints || 4}
                  </span>
                </div>

                <div className="stepper-container">
                  <div className="stepper-progress-bg">
                    <div
                      className="stepper-progress-fill"
                      style={{ width: `${(level / 4) * 100}%` }}
                    />
                  </div>

                  {LEVELS.map((name, index) => (
                    <div
                      key={name}
                      className={`step-node ${index + 1 <= level ? "unlocked" : ""} ${index + 1 === level ? "current" : ""}`}
                    >
                      <div className="step-circle">{index + 1}</div>
                      <div className="step-label">{name}</div>
                    </div>
                  ))}
                </div>

                <div className="hint-actions">
                  <button
                    className="btn-hint"
                    onClick={showNextHint}
                    disabled={busy || !hintData?.hasMoreHints}
                  >
                    {busy ? "Unlocking..." : hintData?.hasMoreHints ? "⚡ Show Next Hint" : "✓ All Hints Unlocked"}
                  </button>
                  <span>{hintData?.message || `Unlocked ${level} of 4 hints`}</span>
                </div>

                <div className="unlocked-hints-stack">
                  {hints.length === 0 ? (
                    <p>No conceptual hints unlocked yet. Click Show Next Hint to reveal Level 1 guidance.</p>
                  ) : hints.map((hint) => (
                    <div className={`hint-card level-${hint.level}`} key={hint.id}>
                      <div className="hint-card-title">
                        {hint.title}
                        <span>Unlocked</span>
                      </div>
                      <div className="hint-card-body">{hint.content}</div>
                      {hint.level === 4 && (
                        <div className="complexity-badges">
                          <span className="complexity-pill">Time: {hint.timeComplexity}</span>
                          <span className="complexity-pill">Space: {hint.spaceComplexity}</span>
                        </div>
                      )}
                    </div>
                  ))}
                </div>
              </div>

              <div className="panel-card">
                <div className="panel-card-header">
                  <h3 className="panel-title">💻 Code Solution Workspace</h3>
                  <div className="language-tabs">
                    {["cpp", "java", "python"].map((lang) => (
                      <button
                        key={lang}
                        className={`lang-tab ${language === lang ? "active" : ""}`}
                        onClick={() => changeLanguage(lang)}
                      >
                        {lang === "cpp" ? "C++" : lang === "java" ? "Java" : "Python"}
                      </button>
                    ))}
                  </div>
                </div>

                <div className="code-editor-area">
                  <textarea
                    className="code-textarea"
                    spellCheck="false"
                    value={code}
                    onChange={(e) => setCode(e.target.value)}
                    placeholder="Write or edit your DSA solution here..."
                  />
                </div>

                <div className="editor-footer">
                  <button
                    className="btn-secondary"
                    onClick={() => setCode(
                      problem.languageOptions?.find(
                        (x) => x.languageKey.toLowerCase() === language
                      )?.starterTemplate || ""
                    )}
                  >
                    Reset to Template
                  </button>
                  <button className="btn-primary" onClick={handleSubmit} disabled={busy}>
                    {busy ? "Submitting..." : "▶ Run & Submit Solution"}
                  </button>
                </div>

                {result && (
                  <div className="submission-result">
                    <div className="result-header">
                      <strong className="result-status">{result.status}</strong>
                      <span className="result-metrics">
                        Runtime: {result.runtime ?? result.executionTimeMs ?? "—"} |
                        Memory: {result.memory ?? result.memoryKb ?? "—"}
                      </span>
                    </div>
                    <div className="result-feedback">
                      {result.feedback || result.message || "Submission received."}
                    </div>
                  </div>
                )}
              </div>
            </section>
          </div>
        </>
      )}
    </div>
  );
}
