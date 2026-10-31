import { useState } from "react";
import FormField from "../FormField";
import api from "../../api/axios";
import Card from "../Card";

export default function SearchMemberFormComponent() {
    const [query, setQuery] = useState("");
    const [error, setError] = useState("");
    const [loading, setLoading] = useState(false);
    const [res, setRes] = useState(null)

    function handleChange(e) {
        setQuery(e.target.value);
    }

    async function handleSubmit(e) {
        e.preventDefault();
        setError("");
        setLoading(true);
        try {
            const responseData = await SearchMember(query)
            setRes(responseData);
        } catch (err) {
            setError(err.response?.data?.message ?? "Could not search member")
        } finally {
            setLoading(false);
            setQuery("")
        }
    }

    function handleReset() {
        setRes(null);
    }
 
    return (
        <div className="m-0 flex justify-cente">
            {res === null ?
            (<div className="m-10 w-xl bg-sky-400 border-none rounded-2xl p-3.5">
               <form onSubmit={handleSubmit}>
                    <FormField label="Search" name="search" value={query} onChange={handleChange} />
                    {error && <p className="text-red-700 font-ibm-plex">{error}</p> }
                    <div className="text-center font-asap-sharp">
                        <button type="submit" disabled={query === "" || loading} className="cursor-pointer bg-green-500 disabled:bg-gray-500 text-white hover:cursor-default hover:p-2.5 disabled:hover:p-2 p-2 rounded-2xl transition-all duration-200">
                            {loading ? "Loading..." : "Search"}
                        </button>
                    </div>
                </form>
            </div>) : (
                    <div className="w-xl flex justify-center flex-col">
                        { res.length === 0 ? (  <p>No Members Found</p> ) :
                            (res.map((member) => (
                                <Card identifier={member.memberID} title={member.name} line1={member.phone} line2={member.email} line3={member.status} color="blue" />
                            ))
                            )
                        }                   
                        <button onClick={handleReset} className=" mt-4 cursor-pointer bg-green-500 text-white hover:cursor-default hover:p-2.5 p-2 rounded-2xl transition-all duration-200">
                            Clear
                        </button>
                    </div>
                    )
            }
        </div>
    )
}

async function SearchMember(query) {
    const res = await api.get(`api/v1/members/search?q=${query}`);
    return res.data;
}