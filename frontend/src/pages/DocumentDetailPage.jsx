import { useEffect, useState, useRef } from 'react';
import { useParams, Link } from 'react-router-dom';
import { DocumentsApi, QnaApi } from '../services/api.js';
import RiskSeal from '../components/RiskSeal.jsx';

const FIELD_LABELS = [
  ['surveyNumber', 'Survey Number'],
  ['propertyAddress', 'Property Address'],
  ['granterName', 'Granter'],
  ['granteeName', 'Grantee'],
  ['executionDate', 'Execution Date'],
  ['registrationNumber', 'Registration No.'],
  ['considerationAmount', 'Consideration'],
];

export default function DocumentDetailPage() {
  const { id } = useParams();
  const [doc, setDoc] = useState(null);
  const [flags, setFlags] = useState([]);
  const [history, setHistory] = useState([]);
  const [question, setQuestion] = useState('');
  const [asking, setAsking] = useState(false);
  const [error, setError] = useState(null);
  const threadRef = useRef(null);

  const load = () => {
    DocumentsApi.get(id).then(setDoc).catch((e) => setError(e?.response?.data?.message || 'Failed to load document.'));
    DocumentsApi.riskFlags(id).then(setFlags).catch(() => {});
    QnaApi.history(id).then(setHistory).catch(() => {});
  };

  useEffect(load, [id]);

  useEffect(() => {
    threadRef.current?.scrollTo({ top: threadRef.current.scrollHeight });
  }, [history]);

  const askQuestion = async (e) => {
    e.preventDefault();
    if (!question.trim() || asking) return;
    setAsking(true);
    const q = question;
    setQuestion('');
    try {
      const res = await QnaApi.ask(id, q);
      setHistory((h) => [...h, { question: res.question, answer: res.answer, id: Date.now() }]);
    } catch (e2) {
      setHistory((h) => [...h, { question: q, answer: e2?.response?.data?.message || 'Something went wrong answering that.', id: Date.now() }]);
    } finally {
      setAsking(false);
    }
  };

  if (error) return <div className="error-banner">{error}</div>;
  if (!doc) return <div className="spinner" />;

  return (
    <>
      <header className="page-header">
        <div className="eyebrow"><Link to="/documents" style={{ color: 'var(--brass)' }}>Document Ledger</Link> / #{doc.id}</div>
        <div style={{ display: 'flex', alignItems: 'center', gap: 16 }}>
          <RiskSeal level={doc.riskLevel} />  // ✅ Correct
          <div>
            <h1 className="page-title">{doc.fileName}</h1>
            <p className="page-sub" style={{ margin: '4px 0 0' }}>
              {doc.documentType || 'Unclassified'} · <span className="status-pill">{doc.status}</span>
              {doc.riskScore != null && ` · Risk score ${doc.riskScore}/100`}
            </p>
          </div>
        </div>
      </header>

      <div className="two-col">
        <div className="stack">
          <div className="parchment-card">
            <h3>Extracted Schema</h3>
            <div className="field-grid">
              {FIELD_LABELS.map(([key, label]) => (
                <div className="field" key={key}>
                  <div className="field-label">{label}</div>
                  <div className="field-value">{doc[key] || '—'}</div>
                </div>
              ))}
            </div>
          </div>

          <div className="panel">
            <h3>Risk Audit Findings</h3>
            {doc.riskSummary && <p className="page-sub" style={{ marginBottom: 14 }}>{doc.riskSummary}</p>}
            {flags.length === 0 ? (
              <div className="empty-state" style={{ padding: '20px 0' }}>
                {doc.status === 'ANALYZED' ? 'No risk indicators found.' : 'Audit still processing…'}
              </div>
            ) : (
              flags.map((f) => (
                <div className="flag-item" key={f.id}>
                  <div className={`flag-dot ${(f.severity || 'medium').toLowerCase()}`} />
                  <div>
                    <div className="flag-category">{f.category}</div>
                    <div className="flag-desc">{f.description}</div>
                    {f.recommendation && <div className="flag-rec">→ {f.recommendation}</div>}
                  </div>
                </div>
              ))
            )}
          </div>
        </div>

        <div className="panel">
          <h3>Ask the Document</h3>
          <p className="page-sub" style={{ marginBottom: 14 }}>
            Answers are grounded strictly in this deed's text — nothing outside it.
          </p>
          <div className="qna-thread" ref={threadRef}>
            {history.length === 0 && (
              <div className="empty-state" style={{ padding: '20px 0' }}>
                Ask something like "Who are the witnesses?" or "Is there a mortgage clause?"
              </div>
            )}
            {history.map((m) => (
              <div key={m.id || m.askedAt}>
                <div className="qna-question">{m.question}</div>
                <div className="qna-answer" style={{ marginTop: 6 }}>{m.answer}</div>
              </div>
            ))}
          </div>
          <form className="qna-form" onSubmit={askQuestion}>
            <input
              className="qna-input"
              placeholder="Ask a question about this deed…"
              value={question}
              onChange={(e) => setQuestion(e.target.value)}
              disabled={asking}
            />
            <button className="btn btn-primary" disabled={asking || !question.trim()}>
              {asking ? <span className="spinner" /> : 'Ask'}
            </button>
          </form>
        </div>
      </div>
    </>
  );
}
