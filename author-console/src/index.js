import React from 'react';
import ReactDOM from 'react-dom/client';
import './index.css';
import reportWebVitals from './reportWebVitals';
import App from "./App";
import {AuthProvider} from "./auth";

ReactDOM.createRoot(document.getElementById('root')).render(
    <AuthProvider>
        <App/>
     </AuthProvider>
);


reportWebVitals();
