import { NavLink } from 'react-router-dom';

const links = [
  { to: '/', label: 'Dashboard', icon: '◈', end: true },
  { to: '/upload', label: 'Upload Deed', icon: '⬆' },
  { to: '/documents', label: 'Document Ledger', icon: '☰' },
];

export default function Sidebar() {
  return (
    <aside className="sidebar">
      <div className="brand">
        <div className="brand-mark">AL</div>
        <div className="brand-text">
          AI Land<br />Analyzer
          <small>Deed Intelligence</small>
        </div>
      </div>

      <ul className="nav-list">
        {links.map((l) => (
          <li key={l.to}>
            <NavLink
              to={l.to}
              end={l.end}
              className={({ isActive }) => 'nav-link' + (isActive ? ' active' : '')}
            >
              <span aria-hidden="true">{l.icon}</span> {l.label}
            </NavLink>
          </li>
        ))}
      </ul>

      <div className="sidebar-footer">
        SPRING AI · GROQ<br />
        LLAMA 3.3 70B<br />
        v1.0.0
      </div>
    </aside>
  );
}
