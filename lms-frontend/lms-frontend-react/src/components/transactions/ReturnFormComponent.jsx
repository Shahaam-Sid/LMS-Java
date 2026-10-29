import { useState } from "react";
import { validators } from "../../utils/validations";
import FormField from "../FormField";
import api from "../../api/axios";
import LiveError from "../LiveError";

export default function ReturnFormComponent() {
    const [ID, setID] = useState("");
    const [validationErrors, setValidationErrors] = useState({});
    const [error, setError] = useState(false);
    const [isInvalid, setIsInvalid] = useState(true);
    const [loading, setLoading] = useState(false);

    function handleChange(e) {
        setID(e.target.value);
        setValidationErrors(validators.ID(e.target.value));
        setIsInvalid(validationErrors.ID !==  "")
    }

    async function handleSubmit(e) {
        e.preventDefault();
        setError("");
        setLoading(true)
        try {
            await returnBook(ID);
        } catch (err) {
            setError(err.response?.data?.message ?? "Could not return book");
            setLoading(false)
        }
    }

    return (
            <div className="m-0 flex justify-cente">
                <div className="m-10 w-xl bg-rose-400 border-none rounded-2xl p-3.5">
                   <form onSubmit={handleSubmit}>
                        <FormField label="Loan ID" name="ID" value={ID} minlength={9} maxlenght={9} onChange={handleChange} />
                        <LiveError errorMsg={validationErrors.ID} />
                        {error && <p className="text-red-700 font-ibm-plex">{error}</p> }
                        <div className="text-center font-asap-sharp">
                            <button type="submit" disabled={isInvalid || loading} className="cursor-pointer bg-green-500 disabled:bg-gray-500 text-white hover:cursor-default hover:p-2.5 disabled:hover:p-2 p-2 rounded-2xl transition-all duration-200">
                                {loading ? "Loading..." : "Search"}
                            </button>
                        </div>
                    </form>
                </div>
            </div>
        );

}

async function returnBook(ID) {
    const res = await api.patch(`api/v1/loans/${ID}`)
}