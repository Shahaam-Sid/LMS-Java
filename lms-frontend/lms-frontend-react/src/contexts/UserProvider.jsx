import { useState } from "react";
import { UserContext } from "./UserContext";
import api from "../api/axios";

export function UserProvider({ children }) {

    const [user, setUser] = useState(() => {
        const stored = localStorage.getItem("token");
        return stored ? {token: stored} : null;
    });

    async function logIn(email, password) {
        const res = await api.post("/api/v1/auth/authenticate", {email, password})
        const {token, admin} = res.data;

        localStorage.setItem("token", token);
        setUser({token, ...admin});
    }

    async function signIn(name, phone, email, address, birthYear, password) {
        const res = await api.post("/api/v1/auth/register",
            {
                name,
                phone,
                email,
                address,
                birthYear,
                password
            }
        );
        const {token, admin} = res.data;
        localStorage.setItem("token", token);
        setUser({token, ...admin});
    }

    async function logOut() {
        localStorage.removeItem("token");
        setUser(null);
    }

    return (
        <UserContext.Provider value={{user, logIn, signIn, logOut}}>
            {children}
        </UserContext.Provider>
    );
}