import './App.css';
import React from 'react';
import { BrowserRouter, Routes, Route } from "react-router-dom";
import AssistantTable from "./AssistantTable";
import AssistantDetail from "./AssistantDetail";
import NewAssistant from "./NewAssistant";
import ChatHistoryTable from "./ChatHistoryTable";
import EmbeddingInstruction from "./EmbeddingInstruction";


function App() {
    return (
        <BrowserRouter>
            <Routes>
                <Route path="/assistant/add" element={<NewAssistant/>}/>
                <Route path="/assistant" element={<AssistantTable />}/>
                <Route path="/assistant/:assistantId" element={<AssistantDetail  />}/>
                <Route path="/assistant/:assistantId/chatHistory" element={<ChatHistoryTable/>}/>
                <Route path="/assistant/:assistantId/instruction" element={<EmbeddingInstruction/>}/>
                <Route path="*" element={<AssistantTable />}/>
            </Routes>
        </BrowserRouter>
    );
}
export default App;
