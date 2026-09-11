import React from 'react';
export function IconButton({ label, color = 'bg-black', stroke = "stroke-white", children, onClick }) {
  
    return (
      <button
        onClick={onClick}
        className="group flex flex-col items-center gap-2 focus:outline-none"
      >
        {/* Container */}
        <div
          className={`flex h-24 w-24 items-center justify-center rounded-3xl transition-all duration-50 group-active:scale-95 shadow-sm ${color}`}
        >
          {/* Render passed SVG icon with dynamic stroke color */}
          {React.cloneElement(children, {
            className: `h-12 w-12 ${stroke}`,
          })}
        </div>
  
        {/* Text Label */}
        <span className="text-base font-normal text-slate-800">{label}</span>
      </button>
    );
  }