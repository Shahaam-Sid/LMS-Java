import { useState } from "react";
import { validators } from "../../utils/validations";
import FormField from "../FormField";
import LiveError from "../LiveError";
import api from "../../api/axios";
import Card from "../Card";

export default function GetTransactionFormComponent() {
    const [UUID, setUUID] = useState("");
    const [validationErrors, setValidationErrors] = useState({});
    const [error, setError] = useState("");
    const [loading, setLoading] = useState(false);
    const [res, setRes] = useState(null);

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
        setLoading(true);
        try {
            const responseData = await GetTransaction(UUID.trim());
            setRes(responseData);
            setUUID("");
        } catch (err) {
            setError(err.response?.data?.message ?? "Could not get transaction");
        } finally {
            setLoading(false);
        }
    }

    function handleReset() {
        setRes(null);
        setError("");
        setValidationErrors({});
    }

    return (
        <div className="m-0 flex justify-center">
            {res === null ? (
                <div className="m-10 w-xl bg-rose-400 border-none rounded-2xl p-3.5">
                    <form onSubmit={handleSubmit}>
                        <FormField label="UUID" name="UUID" value={UUID} minLength={36} maxLength={36} onChange={handleChange} />
                        <LiveError errorMsg={validationErrors.UUID} />
                        {error && <p className="text-red-700 font-ibm-plex">{error}</p>}
                        <div className="text-center font-asap-sharp">
                            <button
                                type="submit"
                                disabled={!isFilled || hasErrors || loading}
                                className="cursor-pointer bg-green-500 disabled:bg-gray-500 text-white hover:cursor-default hover:p-2.5 disabled:hover:p-2 p-2 rounded-2xl transition-all duration-200"
                            >
                                {loading ? "Loading..." : "Get"}
                            </button>
                        </div>
                    </form>
                </div>
            ) : (
                <div className="w-xl flex justify-center flex-col">
                    <Card
                        identifier={res.loanID}
                        title={`Member: ${res.memberID} | Book: ${res.ISBN  }`}
                        line1=""
                        line2={`Borrowed: ${res.borrowDate} | Due: ${res.dueDate}`}
                        line3={res.fineAmount ? res.fineAmount : res.returnDate}
                        color="pink"
                    />
                    <button
                        onClick={handleReset}
                        className="mt-4 cursor-pointer bg-green-500 text-white hover:cursor-default hover:p-2.5 p-2 rounded-2xl transition-all duration-200"
                    >
                        Clear
                    </button>
                </div>
            )}
        </div>
    );
}

async function GetTransaction(UUID) {
    const res = await api.get(`api/v1/loans/${UUID}`);
    return res.data;
}