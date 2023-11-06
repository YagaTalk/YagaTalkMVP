import './App.css';
import React from 'react';
import { BrowserRouter, Routes, Route } from "react-router-dom";
import AssistantTable from "./AssistantTable";
import ContextDetail from "./ContextDetail";
import NewContext from "./NewContext";
import ChatHistoryTable from "./ChatHistoryTable";
import EmbeddingInstruction from "./EmbeddingInstruction";
import {BASE_PATH} from "./Config";


function App() {
    return (
        <BrowserRouter basename={BASE_PATH}>
            <Routes>
                <Route path="/context/add" element={<NewContext/>}/>
                <Route path="/context" element={<AssistantTable />}/>
                <Route path="/context/:contextId" element={<ContextDetail  />}/>
                <Route path="/context/:contextId/chatHistory" element={<ChatHistoryTable/>}/>
                <Route path="/context/:contextId/instruction" element={<EmbeddingInstruction/>}/>
                <Route path="*" element={<AssistantTable />}/>
            </Routes>
        </BrowserRouter>
    );
}
export default App;
