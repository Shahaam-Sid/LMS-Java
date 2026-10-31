import { useState } from "react";
import api from "../../api/axios";
import Card from "../Card";

export default function GetOverdueResponseComponent() {
    const [res, setRes] = useState(null);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState("");

    async function handleSubmit(e) {
        e.preventDefault();
        setError("");
        setLoading(true);
        try {
            const responseData = await getOverdue();
            setRes(responseData ?? []);
        } catch (err) {
            setError(err.response?.data?.message ?? "Could not get overdue transactions");
        } finally {
            setLoading(false);
        }
    }

    function handleReset() {
        setRes(null);
        setError("");
    }

    return (
        <div className="m-0 flex justify-center">
            {res === null ? (
                <>
                    {error && <p className="text-red-700 font-ibm-plex">{error}</p>}
                    <div className="text-center font-asap-sharp">
                        <button type="button" disabled={loading} onClick={handleSubmit} className="cursor-pointer bg-green-500 disabled:bg-gray-500 text-white hover:cursor-default hover:p-2.5 disabled:hover:p-2 p-2 rounded-2xl transition-all duration-200">
                            {loading ? "Loading..." : "Fetch"}
                        </button>
                    </div>
                </>
            ) : (
                <div className="w-xl flex justify-center flex-col">
                    {res.length === 0 ? (
                        <p>No Overdue Transactions Found</p>
                    ) : (
                        res.map((loan) => (
                            <Card
                                key={loan.loanID}
                                identifier={loan.loanID}
                                title={`Member: ${loan.name} | Phone: ${loan.phone}`}
                                line1=""
                                line2={`Borrowed: ${loan.borrowDate} | Due: ${loan.dueDate}`}
                                line3={loan.fineAmount ? loan.fineAmount : loan.returnDate}
                                color="pink"
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

async function getOverdue() {
    const res = await api.get("api/v1/loans/active/overdue");
    return res.data;
}