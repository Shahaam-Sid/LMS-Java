const colorMap = {
    blue: 'text-sky-400',
    green: 'text-emerald-400',
    purple: 'text-purple-500',
    pink: 'text-rose-400'
}

export default function Card({
    identifier = "",
    title = "",
    line1 = "",
    line2 = "",
    line3 = "",
    color = "text-black"
}) {
    return (
        <div className="m-0 my-4 px-7 flex flex-col border-none rounded-2xl p-3.5 shadow-xl/30">
            <p className="font-asap-sharp text-sm">{identifier}</p>
            <p className={`font-ibm-plex text-xl ${colorMap[color]}`}>{title}</p>
            <p className="font-montserrat">{line1}</p>
            {line2 && <p className="font-asap-sharp">{line2}</p>}
            {line3 && <p className="font-asap-sharp">{line3}</p>}
        </div>
    );
}