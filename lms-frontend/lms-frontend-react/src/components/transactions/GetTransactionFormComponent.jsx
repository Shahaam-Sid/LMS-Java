import { useState } from "react";
import { validators } from "../../utils/validations";
import FormField from "../FormField";
import LiveError from "../LiveError";
import api from "../../api/axios";

export default function GetTransactionFormComponent() {
    const [ID, setID] = useState("");
    const [validationErrors, setValidationErrors] = useState({});
    const [error, setError] = useState("");
    const [loading, setLoading] = useState(false);
    const [isInvalid, setIsInvalid] = useState(true);

    function handleChange(e) {
        setID(e.target.value);

        const validate = validators[ID];
        if (validate) {
            const errorMsg = validate(e.target.value);
            setValidationErrors(prev => ({...prev, [ID]: errorMsg}))
        }
        setIsInvalid(Object.values(validationErrors).every(value => !value));
    }

    async function handleSubmit(e) {
        e.preventDefault();
        setError("");
        setLoading(true);
        try {
            await GetTransaction(ID)
        } catch (err) {
            setError(err.response?.data?.message ?? "Could not get book")
        } finally {
            setLoading(false);
        }
    }

    return (
        <div className="m-0 flex justify-center">
            <div className="m-10 w-xl bg-rose-400 border-none rounded-2xl p-3.5">
               <form onSubmit={handleSubmit}>
                    <FormField label="ID" name="ID" value={ID} minlength={36} maxlenght={36} onChange={handleChange} />
                    <LiveError errorMsg={validationErrors.ID} />
                    {error && <p className="text-red-700 font-ibm-plex">{error}</p> }
                    <div className="text-center font-asap-sharp">
                        <button type="submit" disabled={isInvalid || loading} className="cursor-pointer bg-green-500 disabled:bg-gray-500 text-white hover:cursor-default hover:p-2.5 disabled:hover:p-2 p-2 rounded-2xl transition-all duration-200">
                            {loading ? "Loading..." : "Get"}
                        </button>
                    </div>
                </form>
            </div>
        </div>
    )
}

async function GetTransaction(ID) {
    const res = await api.get(`api/v1/loans/${ID}`);
}