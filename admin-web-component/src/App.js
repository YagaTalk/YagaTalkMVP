import './App.css';
import React from 'react';
import { render } from "react-dom";
import { BrowserRouter, Routes, Route } from "react-router-dom";
import Login from './Login';
import Menu from './Menu';
import ContextTable from "./ContextTable";
import ContextDetail from "./ContextDetail";
import NewContext from "./NewContext";
import ChatHistoryTable from "./ChatHistoryTable";
import EmbeddingInstruction from "./EmbeddingInstruction";


const rootElement = document.getElementById("root");


function App() {
    render(
        <BrowserRouter>
            <Routes>
                <Route path="/" element={<Login/>}/>
                <Route path="menu" element={<Menu/>}/>
                <Route path="/context/add" element={<NewContext/>}/>
                <Route path="/context" element={<ContextTable />}/>
                <Route path="/context/:contextId" element={<ContextDetail  />}/>
                <Route path="/context/:contextId/chatHistory" element={<ChatHistoryTable/>}/>
                <Route path="/context/:contextId/instruction" element={<EmbeddingInstruction/>}/>
                <Route path="*" element={<Login/>}/>
            </Routes>
        </BrowserRouter>,
        rootElement
    );
}
export default App;
