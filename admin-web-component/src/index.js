import React from 'react';
import ReactDOM from 'react-dom';
import {AuthProvider} from 'react-oauth2-code-pkce';
import './index.css';
import reportWebVitals from './reportWebVitals';
import App from "./App";
import {KEYCLOAK_URL} from "./Config";

const realm = 'yagatalk';

const authConfig = {
    clientId: 'yagatalk-public-client',
    authorizationEndpoint: `${KEYCLOAK_URL}/realms/${realm}/protocol/openid-connect/auth`,
    tokenEndpoint: `${KEYCLOAK_URL}/realms/${realm}/protocol/openid-connect/token`,
    logoutEndpoint: `${KEYCLOAK_URL}/realms/${realm}/protocol/openid-connect/logout`,
    redirectUri: 'http://localhost:3000',
    scope: 'openid',
    onRefreshTokenExpire: (event) => {
        if (window.confirm('Session expired. Refresh page to continue using the site?')) {
            event.login();
        }
    }
};


const root = ReactDOM.createRoot(document.getElementById('root'));
root.render(
    <AuthProvider authConfig={authConfig}>
        <App/>
    </AuthProvider>
);


reportWebVitals();
