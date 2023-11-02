import './App.css';
import React from 'react';
import { render } from "react-dom";
import { BrowserRouter, Routes, Route } from "react-router-dom";
import AssistantTable from "./AssistantTable";
import ContextDetail from "./ContextDetail";
import NewContext from "./NewContext";
import ChatHistoryTable from "./ChatHistoryTable";
import EmbeddingInstruction from "./EmbeddingInstruction";


const rootElement = document.getElementById("root");


function App() {
    render(
        <BrowserRouter>
            <Routes>
                <Route path="/context/add" element={<NewContext/>}/>
                <Route path="/context" element={<AssistantTable />}/>
                <Route path="/context/:contextId" element={<ContextDetail  />}/>
                <Route path="/context/:contextId/chatHistory" element={<ChatHistoryTable/>}/>
                <Route path="/context/:contextId/instruction" element={<EmbeddingInstruction/>}/>
                <Route path="*" element={<AssistantTable />}/>
            </Routes>
        </BrowserRouter>,
        rootElement
    );
}
export default App;
