import './App.css';
import React from 'react';
import { BrowserRouter, Routes, Route } from "react-router-dom";
import AssistantTable from "./AssistantTable";
import AssistantDetail from "./AssistantDetail";
import NewAssistant from "./NewAssistant";
import ChatHistoryTable from "./ChatHistoryTable";
import EmbeddingInstruction from "./EmbeddingInstruction";
import {BASE_PATH} from "./Config";


function App() {
    return (
        <BrowserRouter basename={BASE_PATH}>
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
