import "./styles.css";
import React, {useEffect} from "react";
import Form from "./component/Form";
import NavBar from "./component/Navigation";

import {Route, Routes} from "react-router-dom";
import Validation from "./Pages/Validation";
import Confirmation from "./Pages/Confirmation";


const ws = new WebSocket('ws://localhost:8888/event-emitter');
let sessionId;
ws.onopen = () => {
    console.log('WebSocket connection opened');
};
ws.onclose = () => {
    console.log('WebSocket connection closed');
};
ws.onmessage = function (event) {
    const message = JSON.parse(event.data)
    if (message.key && message.key === EventEnum.OPEN_SESSION) {
        sessionId = message['value'];
        console.log('Server opened session:', message);
        return;
    }
    console.log('Received message:', message);
};

function send(message, callback) {
    waitForConnection(function () {
        ws.send(message);
        if (typeof callback !== 'undefined') {
            callback();
        }
    }, 1000);
}

function waitForConnection(callback, interval) {
    if (ws.readyState === 1) {
        callback();
    } else {
        // optional: implement backoff for an interval here
        setTimeout(function () {
            waitForConnection(callback, interval);
        }, interval);
    }
}

const EventEnum = {
    OPEN_SESSION: "OPEN_SESSION",
    ADD_EVENT: "ADD_EVENT",
    ERROR: "ERROR"
};

function createMessage(key, value) {
    const jsonObject = {};
    jsonObject['key'] = key;
    jsonObject['value'] = value;
    return JSON.stringify(jsonObject);

}

const withGlobalListeners = (WrappedComponent) => {

    return (props) => {

        useEffect(() => {

            (function () {
                console.error = function () {
                    const errorDetails = Array.from(arguments);
                    if (errorDetails[0]){
                        send(createMessage(EventEnum.ERROR, `${errorDetails[0]}`));
                    }
                };
            }());


            const handleGlobalClick = (event) => {
                if (event.srcElement) {
                    if (event.srcElement.id) {
                        console.log('Clicked id:', event.srcElement.id);
                    } else if (event.srcElement.classList) {
                        console.log('Clicked class:', event.srcElement.classList.value);
                    }

                }
            };

            const handleGlobalFocus = (event) => {
                if (event.target.id) {
                    console.log(`${event.target.id} is focused`);
                    send(createMessage(EventEnum.ADD_EVENT, `${event.target.id} is focused`));
                }
            };
            const handleGlobalBlur = (event) => {
                if (event.target.id) {
                    console.log(`${event.target.id} lost focus`);
                    send(createMessage(EventEnum.ADD_EVENT, `${event.target.id} lost focus`));
                }
            };

            const handleGlobalChange = (event) => {
                if (event.target.type === 'checkbox') {
                    console.log('Global Checkbox Change:', event);
                    send(createMessage(EventEnum.ADD_EVENT, `${event.target.id} was ${event.target.checked ? 'checked' : 'unchecked'}`));
                }
            };
            const handlePageLoad = () => {
                console.log('Page Loaded');
                send(createMessage(EventEnum.OPEN_SESSION, 'Page opened'));
            };


            const handlePageUnload = () => {
                console.log('Page Unloaded');
                send(createMessage(EventEnum.ADD_EVENT, 'Page was closed'));
            };

            let isReloading = false;
            const handleBeforeUnload = () => {
                console.log('Before Page Unloaded');
                send(createMessage(EventEnum.ADD_EVENT, 'Before Page is closed'));
                isReloading = true;
            };

            const handleTabVisibility = () => {
                if (document.visibilityState === 'hidden' && !isReloading) {
                    console.log('User switched to another tab or minimized the browser.');
                    send(createMessage(EventEnum.ADD_EVENT, 'User switched to another tab or minimized the browser.'));
                } else if (document.visibilityState === 'visible' && !isReloading) {
                    console.log('User came back to the tab.');
                    send(createMessage(EventEnum.ADD_EVENT, 'User came back to the tab.'));
                }
            }

            document.addEventListener('click', handleGlobalClick);
            window.addEventListener('focus', handleGlobalFocus, true); // 'true' to capture focus events during the capture phase
            window.addEventListener('blur', handleGlobalBlur, true); // 'true' to capture blur events during the capture phase
            window.addEventListener('change', handleGlobalChange);
            window.addEventListener('load', handlePageLoad);
            window.addEventListener('beforeunload', handleBeforeUnload);
            window.addEventListener('unload', handlePageUnload);
            window.addEventListener('visibilitychange', handleTabVisibility);

            return () => {
                document.removeEventListener('click', handleGlobalClick);
                window.removeEventListener('focus', handleGlobalFocus, true);
                window.removeEventListener('blur', handleGlobalBlur, true);
                window.removeEventListener('change', handleGlobalChange);
                window.removeEventListener('load', handlePageLoad);
                window.removeEventListener('beforeunload', handleBeforeUnload);
                window.removeEventListener('unload', handlePageUnload);
                window.removeEventListener('visibilitychange', handleTabVisibility);

            };
        }, []);

        return <WrappedComponent {...props} />;
    };
};

const App = () => (
    <div className="App">
        <NavBar/>
        <div className="container">{/* <Form /> */}</div>
        <Routes>
            <Route path="/" element={<Form/>}/>
            <Route path="/Confirmation" element={<Confirmation/>}/>
            <Route path="/Validation" element={<Validation/>}/>
        </Routes>
    </div>
);

export default withGlobalListeners(App)
