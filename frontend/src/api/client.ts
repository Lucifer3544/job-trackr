import axios from 'axios';
export const api = axios.create({baseURL: import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080/api', headers:{'Content-Type':'application/json'}});
api.interceptors.response.use(r=>r, e=>{const msg=e.response?.data?.message || e.response?.data?.error || e.message || 'Something went wrong'; return Promise.reject(new Error(msg));});
export const unwrap = <T,>(p:Promise<{data:T}>)=>p.then(r=>r.data);
