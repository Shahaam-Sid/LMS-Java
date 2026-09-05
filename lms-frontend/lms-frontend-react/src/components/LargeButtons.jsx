import { Link } from "react-router-dom";

const colorMap = {
    blue: 'bg-blue-500 text-white hover:bg-blue-700 text-white',
    green: 'bg-green-500 text-white hover:bg-green-700 text-white',
};


export default function LargeButtons({ color = 'green', children, link}) {
    return (
        <Link to={link}>
            <button className={`font-montserrat m-1.5 text-3xl w-60 h-15 rounded bor ${colorMap[color]}`}>
                {children}
            </button>
        </Link>

    );
}