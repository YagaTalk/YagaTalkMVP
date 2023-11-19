import React from 'react';
import ReactDOM from 'react-dom/client';
import './index.css';
import reportWebVitals from './reportWebVitals';
import App from "./App";
import {AuthProvider} from "./auth";

// const realm = 'yagatalk';

// const auth/*Config = {
//     clientId: 'yagatalk-public-client',
//     authorizationEndpoint: `${KEYCLOAK_URL}/realms/${realm}/protocol/openid-connect/auth`,
//     tokenEndpoint: `${KEYCLOAK_URL}/realms/${realm}/protocol/openid-connect/token`,
//     logoutEndpoint: `${KEYCLOAK_URL}/realms/${realm}/protocol/openid-connect/logout`,
//     redirectUri: `${window.origin}${BASE_PATH}`,
//     scope: 'openid',
//     onRefreshTokenExpire: (event) => {
//         if (window.confirm('Session expired. Refresh page to continue using the site?')) {
//             event.login();
//         }
//     }
// };*/

ReactDOM.createRoot(document.getElementById('root')).render(
    <AuthProvider>
        <App/>
     </AuthProvider>
);


reportWebVitals();
