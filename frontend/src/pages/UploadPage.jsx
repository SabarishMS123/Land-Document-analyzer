import { useCallback, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { DocumentsApi } from '../services/api.js';

export default function UploadPage() {
  const [dragging, setDragging] = useState(false);
  const [file, setFile] = useState(null);
  const [status, setStatus] = useState('idle'); // idle | uploading | analyzing | error
  const [error, setError] = useState(null);
  const navigate = useNavigate();

  const handleFiles = (files) => {
    const f = files?.[0];
    if (!f) return;
    if (f.type !== 'application/pdf') {
      setError('Only PDF deed files are supported.');
      return;
    }
    setError(null);
    setFile(f);
  };

  const onDrop = useCallback((e) => {
    e.preventDefault();
    setDragging(false);
    handleFiles(e.dataTransfer.files);
  }, []);

  const submit = async () => {
    if (!file) return;
    setStatus('uploading');
    setError(null);
    try {
      // The single call runs extraction + risk audit synchronously server-side.
      setTimeout(() => setStatus('analyzing'), 800);
      const result = await DocumentsApi.upload(file);
      navigate(`/documents/${result.id}`);
    } catch (e) {
      setStatus('error');
      setError(e?.response?.data?.message || 'Upload failed. Is the backend running?');
    }
  };

  return (
    <>
      <header className="page-header">
        <div className="eyebrow">Intake</div>
        <h1 className="page-title">Upload a Deed</h1>
        <p className="page-sub">
          Drop a sale, gift, mortgage, lease, or partition deed PDF. The pipeline extracts a structured
          schema, then runs an automated title-risk audit against it.
        </p>
      </header>

      {error && <div className="error-banner">{error}</div>}

      <div
        className={'dropzone' + (dragging ? ' dragging' : '')}
        onDragOver={(e) => { e.preventDefault(); setDragging(true); }}
        onDragLeave={() => setDragging(false)}
        onDrop={onDrop}
      >
        <div className="dropzone-icon" aria-hidden="true">⬆</div>
        {file ? (
          <>
            <p style={{ fontWeight: 600 }}>{file.name}</p>
            <p className="page-sub" style={{ margin: '6px auto 0' }}>{(file.size / 1024).toFixed(0)} KB — ready to analyze</p>
          </>
        ) : (
          <>
            <p style={{ fontWeight: 600 }}>Drag a deed PDF here</p>
            <p className="page-sub" style={{ margin: '6px auto 0' }}>or choose a file from your device</p>
          </>
        )}

        <div style={{ marginTop: 20, display: 'flex', gap: 12, justifyContent: 'center' }}>
          <label className="btn btn-ghost">
            Choose File
            <input
              type="file"
              accept="application/pdf"
              onChange={(e) => handleFiles(e.target.files)}
              style={{ display: 'none' }}
            />
          </label>
          <button className="btn btn-primary" disabled={!file || status === 'uploading' || status === 'analyzing'} onClick={submit}>
            {status === 'uploading' && 'Uploading…'}
            {status === 'analyzing' && (<><span className="spinner" /> Analyzing with Llama 3.3…</>)}
            {(status === 'idle' || status === 'error') && 'Analyze Deed'}
          </button>
        </div>
      </div>
    </>
  );
}
