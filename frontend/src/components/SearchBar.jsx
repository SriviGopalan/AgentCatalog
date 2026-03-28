import './SearchBar.css'

export default function SearchBar({ value, onChange }) {
  return (
    <div className="search-bar-wrapper">
      <div className="search-bar">
        <span className="search-icon">🔎</span>
        <input
          className="search-input"
          type="text"
          placeholder="Search agents by name or description..."
          value={value}
          onChange={e => onChange(e.target.value)}
        />
      </div>
    </div>
  )
}
