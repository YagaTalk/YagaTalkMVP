import React, {useEffect, useRef, useState} from 'react';
import './Chat.css';
import {BACKEND_URL, BASE_PATH} from "./Config";


function Chat() {
    const [assistantId, setAssistantId] = useState(null)
    const [error, setError] = useState(null)
    const [sessionId, setSessionId] = useState(null)
    const chatBodyRef = useRef(null);
    const txtInputRef = useRef(null);
    const [messages, setMessages] = useState([]);
    const [shouldScrollToBottom, setShouldScrollToBottom] = useState(true);

    useEffect(function resolveAssistantId() {
        if (!!assistantId) return

        const path = window.location.pathname
        console.log("resolving assistant id from path=" + path)
        if (!path) {
            setError("Failed to resolve assistant id from current location path")
            return
        }
        if (!BASE_PATH) {
            const lastSegmentOfPath = path.replace("/embeddable-chat/", "")
            if (!lastSegmentOfPath) {
                setError("Failed to resolve assistant id from current location path: path is empty")
                return
            }
            console.log("resolved assistant id=" + lastSegmentOfPath)
            setAssistantId(lastSegmentOfPath)
        }
        else {
            const lastSegmentOfPath = path.substring(path.lastIndexOf(BASE_PATH) + BASE_PATH.length + 1).replace("/", "")
            if (!lastSegmentOfPath) {
                setError("Failed to resolve assistant id from current location path: path is empty")
                return
            }
            console.log("resolved assistant id=" + lastSegmentOfPath)
            setAssistantId(lastSegmentOfPath)
        }

    }, [assistantId])

    useEffect(() => {
        if (!assistantId) return
        if (!!sessionId) return

        getChatSession(assistantId)
    }, [sessionId, assistantId])

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

    const getChatSession = async (assistantId) => {
        try {
            const response = await fetch(`${BACKEND_URL}/api/sessions/current?assistantId=${assistantId}`);
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
            const response = await fetch(`${BACKEND_URL}/api/sessions/${chatSessionId}/messages`);
            if (response.ok) {
                const data = await response.json();
                const formattedMessages = data.map(({role, content}) => ({role, text: content}));
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
            const response = await fetch(`${BACKEND_URL}/api/sessions/${chatSessionId}/messages`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({text: userInput})
            });
            if (!response.ok) {
                console.error('Failed to send the user message to the server.');
            }
        } catch (error) {
            console.error('Error occurred while sending the user message:', error);
        }
    };

    const addMessage = (txt, type) => {
        setMessages(prevMessages => [...prevMessages, {role: type, text: txt}]);
    };

    useEffect(() => {
        // Set the scroll position to the bottom if shouldScrollToBottom is true
        if (shouldScrollToBottom && chatBodyRef.current) {
            chatBodyRef.current.scrollTop = chatBodyRef.current.scrollHeight;
        }
    }, [shouldScrollToBottom]);

    const handleKeyPress = (event) => {
        if (event.key === 'Enter') {
            renderUserMessage();
        }
    };

    return <>
        {error && <h1>{error}</h1>}
        {!error && sessionId && (
            <div className="container">
                <div className="chat-header">
                    <div className="logo">
                        <img src={`${BASE_PATH}/images/icon.jpg`} alt="icon"/>
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
                        <textarea
                            ref={txtInputRef}
                            type="text"
                            id="txtInput"
                            placeholder="Let's talk!"
                            autoFocus
                            onKeyDown={handleKeyPress}
                        />
                    </div>
                    <button onClick={renderUserMessage} className="send">
                        <img src={`${BASE_PATH}/images/send.svg`} alt="send"/>
                    </button>
                </div>
            </div>
        )}
    </>;
}

export default Chat;