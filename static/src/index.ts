import { listen } from '@codingame/monaco-jsonrpc';
import * as monaco from 'monaco-editor'
import {
    MonacoLanguageClient, MessageConnection, CloseAction, ErrorAction,
    MonacoServices, createConnection
} from 'monaco-languageclient';
// @ts-ignore
import normalizeUrl = require('normalize-url');
const ReconnectingWebSocket = require('reconnecting-websocket');

const editor = monaco.editor.create(document.getElementById("container")!, {
    model: monaco.editor.createModel("", 'cpp', monaco.Uri.parse('file://D://Projects/CodeExample/example.cpp')),
    theme: "vs-dark",
    accessibilityPageSize: 0,
    minimap: {
        enabled: false,
    },
    mouseWheelZoom: true,
    lightbulb: {
        enabled: true,
    }
});

window.onresize = function () {
    editor.layout()
}
// @ts-ignore
MonacoServices.install(monaco, {rootUri: "file://D://Projects/CodeExample"})

const url = createUrl('ws://localhost:3000/clangd')
const webSocket = createWebSocket(url);

listen({
    webSocket,
    onConnection: connection => {
        const languageClient = createLanguageClient(connection, 'cpp');
        const disposable = languageClient.start();
        connection.onClose(() => disposable.dispose());
    }
});

function createLanguageClient(connection: MessageConnection, language: string): MonacoLanguageClient {
    return new MonacoLanguageClient({
        name: "Monaco Language Client",
        clientOptions: {
            // use a language id as a document selector
            documentSelector: [language],
            // disable the default error handler
            errorHandler: {
                error: () => ErrorAction.Continue,
                closed: () => CloseAction.DoNotRestart
            }
        },
        // create a language client connection from the JSON RPC connection on demand
        connectionProvider: {
            get: (errorHandler, closeHandler) => {
                // @ts-ignore
                return Promise.resolve(createConnection(connection, errorHandler, closeHandler))
            }
        }
    });
}

function createUrl(path: string): string {
    /*const protocol = location.protocol === 'https:' ? 'wss' : 'ws';
    return normalizeUrl(`${protocol}://${location.host}${location.pathname}${path}`);*/
    return normalizeUrl(path);
}

function createWebSocket(url: string): WebSocket {
    const socketOptions = {
        maxReconnectionDelay: 10000,
        minReconnectionDelay: 1000,
        reconnectionDelayGrowFactor: 1.3,
        connectionTimeout: 10000,
        maxRetries: Infinity,
        debug: false
    };
    return new ReconnectingWebSocket(url, [], socketOptions);
}
