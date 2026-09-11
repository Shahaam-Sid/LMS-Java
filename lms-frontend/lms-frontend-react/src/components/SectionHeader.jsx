export default function SectionHeader({ text="Test", color="text-black", colorRule="border-black" }) {
    return(
        <div className="m-5">
            <h2 className={`${color} font-montserrat text-2xl`}>{text}</h2>
            <hr className={`${colorRule} border-t-2 radius rounded-4xl`} />
        </div>
    )
}