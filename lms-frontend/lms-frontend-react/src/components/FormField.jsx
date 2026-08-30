export default function FormField({ label, type="text", value, onChange, name, required=true }){
    return (
        <div className="mb-4 ml-4 mr-4">
            <label className="block text-sm font-medium mb-1" htmlFor={name}>
                {label}
            </label>
                <input id={name}
                name={name}
                type={type}
                value={value}
                onChange={onChange}
                required={required}
                className="w-full border rounded px-3 py-2 focus:outline-none focus:ring-2 focus:ring-blue-500"
                />
        </div>
    );
}