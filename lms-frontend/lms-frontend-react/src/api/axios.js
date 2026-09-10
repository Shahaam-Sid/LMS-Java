import axios from "axios";

const api = axios.create({
    baseURL: "http://localhost:8080",
    timeout: 5000
})

api.interceptors.request.use(config => {
    const token = localStorage.getItem("token");
    if (token) {
        config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
});

let onUnauthorized = () => {};

export function setOnUnauhtorized(handler) {
    onUnauthorized = handler;
}

api.interceptors.response.use(
    (response) => response,
    (error) => {
        if (error.response?.status === 401) {
            onUnauthorized();
        }
        return Promise.reject(error);
    }
);

export default api;