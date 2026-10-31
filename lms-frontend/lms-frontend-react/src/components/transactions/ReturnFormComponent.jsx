import { useState } from "react";
import { validators } from "../../utils/validations";
import FormField from "../FormField";
import api from "../../api/axios";
import LiveError from "../LiveError";

export default function ReturnFormComponent() {
    const [UUID, setUUID] = useState("");
    const [validationErrors, setValidationErrors] = useState({});
    const [error, setError] = useState("");
    const [success, setSuccess] = useState("");
    const [loading, setLoading] = useState(false);

    const hasErrors = Object.values(validationErrors).some(msg => msg);
    const isFilled = UUID.trim() !== "";

    function handleChange(e) {
        const { name, value } = e.target;
        setUUID(value);

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
            await returnBook(UUID.trim());
            setUUID("");
        } catch (err) {
            setError(err.response?.data?.message ?? "Could not return book");
        } finally {
            setLoading(false);
        }
    }

    return (
        <div className="m-0 flex justify-center">
            <div className="m-10 w-xl bg-rose-400 border-none rounded-2xl p-3.5">
                <form onSubmit={handleSubmit}>
                    <FormField label="Loan ID" name="UUID" value={UUID} minLength={36} maxLength={36} onChange={handleChange} />
                    <LiveError errorMsg={validationErrors.UUID} />
                    {error && <p className="text-red-700 font-ibm-plex">{error}</p>}
                    {success && <p className="text-green-800 font-ibm-plex">{success}</p>}
                    <div className="text-center font-asap-sharp">
                        <button type="submit" disabled={!isFilled || hasErrors || loading} className="cursor-pointer bg-green-500 disabled:bg-gray-500 text-white hover:cursor-default hover:p-2.5 disabled:hover:p-2 p-2 rounded-2xl transition-all duration-200">
                            {loading ? "Loading..." : "Return"}
                        </button>
                    </div>
                </form>
            </div>
        </div>
    );
}

async function returnBook(UUID) {
    const res = await api.patch(`api/v1/loans/${UUID}`);
    return res.data;
}