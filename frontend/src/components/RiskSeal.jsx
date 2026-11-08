export default function RiskSeal({ level, small }) {
  const lvl = (level || 'UNKNOWN').toLowerCase();
  const label = lvl === 'unknown' ? 'N/A' : lvl.slice(0, 3).toUpperCase();
  return (
    <div className={`seal risk-${lvl}${small ? ' small' : ''}`} title={`Risk level: ${level || 'Unknown'}`}>
      <span className="seal-label">{label}</span>
    </div>
  );
}
