export default function FormField({ label, type="text", value, onChange, name, required=true, minlength=0, maxlenght=355 }){
    return (
        <div className="mb-4 ml-4 mr-4">
            <label className="font-asap-sharp block text-sm font-medium mb-1" htmlFor={name}>
                {label}
            </label>
                <input id={name}
                name={name}
                type={type}
                value={value}
                onChange={onChange}
                required={required}
                min={1930}
                minLength={minlength}
                maxLength={maxlenght}
                max={`${new Date().getFullYear() - 18}`}
                className="w-full border rounded px-3 py-2 bg-white focus:outline-none focus:ring-2 focus:ring-blue-500"
                />
        </div>
    );
}