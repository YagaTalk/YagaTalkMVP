import React, { useRef, useState } from 'react';
import './App.css';

const testMessages = [
    { role: "user", text: "Hello" },
    { role: "assistant", text: "Hi, can I help you?" },
    { role: "user", text: "Maybe" },
    { role: "assistant", text: "I'm listening carefully to you!" },
    { role: "assistant", text: "Let's talk!" },
    { role: "user", text: "Nice" }
];

function App() {
    const chatBodyRef = useRef(null);
    const txtInputRef = useRef(null);
    const [messages, setMessages] = useState(testMessages);

    const getHistory = () => {
        setMessages(testMessages);
        setScrollPosition();
    };

    const renderUserMessage = () => {
        const userInput = txtInputRef.current.value;
        addMessage(userInput, "user");
        txtInputRef.current.value = "";
        setTimeout(() => {
            renderChatbotResponse(userInput);
            setScrollPosition();
        }, 600);
    };

    const renderChatbotResponse = (userInput) => {
        const res = getChatBotResponse(userInput);
        addMessage(res, "assistant");
    };

    const addMessage = (txt, type) => {
        setMessages(prevMessages => [...prevMessages, { role: type, text: txt }]);
    };

    const getChatBotResponse = (userInput) => {
        return "Message from OpenAI";
    };

    const setScrollPosition = () => {
        if (chatBodyRef.current) {
            chatBodyRef.current.scrollTop = chatBodyRef.current.scrollHeight;
        }
    };

    return (
        <div className="container">
            <div className="chat-header">
                <div className="logo">
                    <img src="/images/icon.jpg" alt="icon" />
                </div>
                <div className="title">YagaTalk</div>
            </div>
            <div ref={chatBodyRef} className="chat-body">
                {messages.map((message, index) => (
                    <div key={index} className={message.role === "user" ? "user-message" : "assistant-message"}>
                        {message.text}
                    </div>
                ))}
            </div>
            <div className="chat-input">
                <div className="input-sec">
                    <input ref={txtInputRef} type="text" id="txtInput" placeholder="Let's talk!" autoFocus />
                </div>
                <button onClick={renderUserMessage} className="send">
                    <img src="/images/send.svg" alt="send" />
                </button>
            </div>
        </div>
    );
}

export default App;