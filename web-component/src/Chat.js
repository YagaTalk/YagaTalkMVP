import React, {useEffect, useRef, useState} from 'react';
import './Chat.css';
import {BACKEND_URL} from "./Config";

const DEFAULT_CONTEXT_ID = "a1e7e851-505b-4b62-b4de-5a56d46ee843"

function resolveContextId() {
    const path = window.location.pathname
    if (!path) return DEFAULT_CONTEXT_ID
    const lastSegmentOfPath = path.split("/")[-1]
    if (!lastSegmentOfPath) return DEFAULT_CONTEXT_ID
    return lastSegmentOfPath
}

function Chat() {
    const [contextId] = useState(resolveContextId())
    const [sessionId, setSessionId] = useState(null)
    const chatBodyRef = useRef(null);
    const txtInputRef = useRef(null);
    const [messages, setMessages] = useState([]);
    const [shouldScrollToBottom, setShouldScrollToBottom] = useState(true);

    useEffect(() => {
        if (!!sessionId) return

        getChatSession(contextId)
    }, [sessionId, contextId])

    useEffect(() => {
        if (!sessionId) return

        getHistory(sessionId);
        const intervalId = setInterval(() => getHistory(sessionId), 1000);
        return () => {
            clearInterval(intervalId);
        };
    }, [sessionId]);

    useEffect(() => {
        if (chatBodyRef.current) {
            const { scrollHeight, clientHeight, scrollTop } = chatBodyRef.current;
            setShouldScrollToBottom(scrollHeight - clientHeight === scrollTop);
        }
    }, [messages]);

    const getChatSession = async (contextId) => {
        try {
            const response = await fetch(`${BACKEND_URL}/api/chat/context/${contextId}/currentSession`);
            if (response.ok) {
                const body = await response.json()
                setSessionId(body.id);
            } else {
                console.error('Failed to fetch current session');
            }
        } catch (error) {
            console.error('Error occurred while fetching current session:', error);
        }
    }

    const getHistory = async (chatSessionId) => {
        try {
            const response = await fetch(`${BACKEND_URL}/api/chat/sessions/${chatSessionId}/messages`);
            if (response.ok) {
                const data = await response.json();
                const formattedMessages = data.map(({ role, content }) => ({ role, text: content }));
                setMessages(formattedMessages);
            } else {
                console.error('Failed to fetch messages from the backend.');
            }
        } catch (error) {
            console.error('Error occurred while fetching messages:', error);
        }
    };

    const renderUserMessage = () => {
        const userInput = txtInputRef.current.value;
        addMessage(userInput, "user");
        txtInputRef.current.value = "";
        sendUserMessageToServer(userInput, sessionId);
    };

    const sendUserMessageToServer = async (userInput, chatSessionId) => {
        try {
            const response = await fetch(`${BACKEND_URL}/api/chat/sessions/${chatSessionId}/messages`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({ text: userInput })
            });
            if (!response.ok) {
                console.error('Failed to send the user message to the server.');
            }
        } catch (error) {
            console.error('Error occurred while sending the user message:', error);
        }
    };

    const addMessage = (txt, type) => {
        setMessages(prevMessages => [...prevMessages, { role: type, text: txt }]);
    };

    useEffect(() => {
        // Set the scroll position to the bottom if shouldScrollToBottom is true
        if (shouldScrollToBottom && chatBodyRef.current) {
            chatBodyRef.current.scrollTop = chatBodyRef.current.scrollHeight;
        }
    }, [shouldScrollToBottom]);

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
                <button disabled={!sessionId} onClick={renderUserMessage} className="send">
                    <img src="/images/send.svg" alt="send" />
                </button>
            </div>
        </div>
    );
}

export default Chat;