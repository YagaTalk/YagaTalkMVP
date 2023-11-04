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
                <Route path="/assistants/add" element={<NewAssistant/>}/>
                <Route path="/assistants" element={<AssistantTable />}/>
                <Route path="/assistants/:assistantId" element={<AssistantDetail  />}/>
                <Route path="/assistants/:assistantId/chatHistory" element={<ChatHistoryTable/>}/>
                <Route path="/assistants/:assistantId/instruction" element={<EmbeddingInstruction/>}/>
                <Route path="*" element={<AssistantTable />}/>
            </Routes>
        </BrowserRouter>
    );
}
export default App;
