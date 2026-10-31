import { useState } from "react";
import { validators } from "../../utils/validations";
import FormField from "../FormField";
import LiveError from "../LiveError";
import api from "../../api/axios";
import Card from "../Card";

export default function GetMemberResevationsFormComponent() {
    const [ID, setID] = useState("");
    const [validationErrors, setValidationErrors] = useState({});
    const [error, setError] = useState("");
    const [loading, setLoading] = useState(false);
    const [res, setRes] = useState(null);

    const hasErrors = Object.values(validationErrors).some(msg => msg);
    const isFilled = ID.trim() !== "";

    function handleChange(e) {
        const { name, value } = e.target;
        setID(value);

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
            const responseData = await GetMemberReservations(ID.trim());
            setRes(responseData ?? []);
            setID("");
        } catch (err) {
            setError(err.response?.data?.message ?? "Could not get reservations");
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
                <div className="m-10 w-xl bg-purple-400 border-none rounded-2xl p-3.5">
                    <form onSubmit={handleSubmit}>
                        <FormField label="ID" name="ID" value={ID} minLength={9} maxLength={9} onChange={handleChange} />
                        <LiveError errorMsg={validationErrors.ID} />
                        {error && <p className="text-red-700 font-ibm-plex">{error}</p>}
                        <div className="text-center font-asap-sharp">
                            <button type="submit" disabled={!isFilled || hasErrors || loading} className="cursor-pointer bg-green-500 disabled:bg-gray-500 text-white hover:cursor-default hover:p-2.5 disabled:hover:p-2 p-2 rounded-2xl transition-all duration-200">
                                {loading ? "Loading..." : "Get"}
                            </button>
                        </div>
                    </form>
                </div>
            ) : (
                <div className="w-xl flex justify-center flex-col">
                    {res.length === 0 ? (
                        <p>No Reservations Found</p>
                    ) : (
                        res.map((reservation) => (
                            <Card
                                identifier={reservation.memberId}
                                title={reservation.isbn}
                                line1={reservation.queuePosition}
                                color="purple"
                            />
                        ))
                    )}
                    <button onClick={handleReset} className="mt-4 cursor-pointer bg-green-500 text-white hover:cursor-default hover:p-2.5 p-2 rounded-2xl transition-all duration-200">
                        Clear
                    </button>
                </div>
            )}
        </div>
    );
}

async function GetMemberReservations(ID) {
    const res = await api.get(`api/v1/reservations/member/${ID}`);
    return res.data;
}