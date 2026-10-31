import { useState } from "react";
import { validators } from "../../utils/validations";
import FormField from "../FormField";
import LiveError from "../LiveError";
import api from "../../api/axios";
import Card from "../Card";

export default function GetBookFormComponent() {
    const [ISBN, setISBN] = useState("");
    const [validationErrors, setValidationErrors] = useState({});
    const [error, setError] = useState("");
    const [loading, setLoading] = useState(false);
    const [res, setRes] = useState(null);

    const hasErrors = validationErrors[ISBN] !== ""

    function handleChange(e) {
        setISBN(e.target.value);

        const validate = validators[ISBN];
        if (validate) {
            const errorMsg = validate(e.target.value);
            setValidationErrors(prev => ({...prev, [ISBN]: errorMsg}))
        }
    }

    async function handleSubmit(e) {
        e.preventDefault();
        setError("");
        setLoading(true);
        try {
            const responseData = await GetBook(ISBN);
            setRes(responseData);
        } catch (err) {
            setError(err.response?.data?.message ?? "Could not get book")
        } finally {
            setLoading(false);
        }
    }

    function handleReset() {
        setRes(null);
        setISBN("");
    }

    return (
        <div className="m-0 flex justify-center">
            
            { res === null ? 
                (<div className="m-10 w-xl bg-emerald-400 border-none rounded-2xl p-3.5">
                <form onSubmit={handleSubmit}>
                        <FormField label="ISBN" name="ISBN" value={ISBN} minlength={10} maxlenght={13} onChange={handleChange} />
                        <LiveError errorMsg={validationErrors.ISBN} />
                        {error && <p className="text-red-700 font-ibm-plex">{error}</p> }
                        <div className="text-center font-asap-sharp">
                            <button type="submit" disabled={!hasErrors || loading} className="cursor-pointer bg-green-500 disabled:bg-gray-500 text-white hover:cursor-default hover:p-2.5 disabled:hover:p-2 p-2 rounded-2xl transition-all duration-200">
                                {loading ? "Loading..." : "Get"}
                            </button>
                        </div>
                    </form>
                </div>) :
                (
                    <div className="w-xl flex justify-center flex-col">
                        <Card identifier={res.ISBN} title={res.title} line1={res.author} line2={res.genre} line3={res.type} color="green" />
                        
                        <button onClick={handleReset} className=" mt-4 cursor-pointer bg-green-500 text-white hover:cursor-default hover:p-2.5 p-2 rounded-2xl transition-all duration-200">
                                Clear
                        </button>
                    </div>
                )
            }
        </div>
    )
}

async function GetBook(ISBN) {
    const res = await api.get(`api/v1/books/${ISBN}`);
    console.log(res);
    return res.data;
}