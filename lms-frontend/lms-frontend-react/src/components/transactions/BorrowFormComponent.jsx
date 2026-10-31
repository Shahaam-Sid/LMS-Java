import { useState } from "react";
import { validators } from "../../utils/validations";
import FormField from "../FormField";
import api from "../../api/axios";
import LiveError from "../LiveError";

export default function BorrowFormComponent() {
    const [ID, setID] = useState("");
    const [ISBN, setISBN] = useState("");
    const [validationErrors, setValidationErrors] = useState({});
    const [error, setError] = useState("");
    const [success, setSuccess] = useState("");
    const [loading, setLoading] = useState(false);

    const hasErrors = Object.values(validationErrors).some(msg => msg);
    const isFilled = ID.trim() !== "" && ISBN.trim() !== "";

    function handleChange(e) {
        const { name, value } = e.target;

        if (name === "ID") setID(value);
        if (name === "ISBN") setISBN(value);

        const validate = validators[name];
        if (validate) {
            setValidationErrors(prev => ({ ...prev, [name]: validate(value) }));
        }
    }

    async function handleSubmit(e) {
        e.preventDefault();
        setError("");
        setSuccess("");
        setLoading(true);
        try {
            await borrow(ID.trim(), ISBN.trim());
            setID("");
            setISBN("");
        } catch (err) {
            setError(err.response?.data?.message ?? "Could not borrow");
        } finally {
            setLoading(false);
        }
    }

    return (
        <div className="m-0 flex justify-center">
            <div className="m-10 w-xl bg-rose-400 border-none rounded-2xl p-3.5">
                <form onSubmit={handleSubmit}>
                    <FormField label="Member ID" name="ID" value={ID} minLength={9} maxLength={9} onChange={handleChange} />
                    <LiveError errorMsg={validationErrors.ID} />
                    <FormField label="ISBN" name="ISBN" value={ISBN} minLength={10} maxLength={13} onChange={handleChange} />
                    <LiveError errorMsg={validationErrors.ISBN} />
                    {error && <p className="text-red-700 font-ibm-plex">{error}</p>}
                    {success && <p className="text-green-800 font-ibm-plex">{success}</p>}
                    <div className="text-center font-asap-sharp">
                        <button type="submit" disabled={!isFilled || hasErrors || loading} className="cursor-pointer bg-green-500 disabled:bg-gray-500 text-white hover:cursor-default hover:p-2.5 disabled:hover:p-2 p-2 rounded-2xl transition-all duration-200">
                            {loading ? "Loading..." : "Borrow"}
                        </button>
                    </div>
                </form>
            </div>
        </div>
    );
}

async function borrow(member_id, isbn) {
    const res = await api.post("api/v1/loans", { member_id, isbn });
    return res.data;
}