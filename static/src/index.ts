import ReconnectingWebsocket = require("reconnecting-websocket");
// @ts-ignore
import normalizeUrl = require('normalize-url');

require('monaco-editor');
(self as any).MonacoEnvironment = {
    getWorkerUrl: () => '/editor/scripts/editor.worker.bundle.js'
}
import {listen} from '@codingame/monaco-jsonrpc';
import * as monaco from 'monaco-editor-core'

import {
    CloseAction,
    createConnection,
    ErrorAction,
    MessageConnection,
    MonacoLanguageClient,
    MonacoServices
} from 'monaco-languageclient';

const ReconnectingWebSocket = require('reconnecting-websocket');

const editor = monaco.editor.create(document.getElementById("container")!, {
    theme: "vs-dark",
    accessibilityPageSize: 0,
    minimap: {
        enabled: false,
    },
    mouseWheelZoom: true,
    lightbulb: {
        enabled: true,
    },
    inlayHints: {
        enabled: true,
    },
});


window.onresize = function () {
    editor.layout()
}

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

let webSocket: ReconnectingWebsocket | null = null;
// @ts-ignore
/*let monacoService: MonacoServices | null = null;*/

function change_monaco_lang(lang: string) {
    const url = createUrl(`ws://localhost:3000/${lang}`)
    if (webSocket != null) {
        const socket: any = webSocket;
        socket.close(1000, '', {keepClosed: true});
        webSocket = null;
    }
    /*if (monacoService != null){
        monacoService.dispose()
    }*/

    webSocket = createWebSocket(url);

    listen({
        webSocket,
        onConnection: connection => {
            const languageClient = createLanguageClient(connection, lang);
            const disposable = languageClient.start();
            connection.onClose(() => {
                disposable.dispose()
                console.log('closing websocket')
            });
        }
    });
    let uri = "";
    let rootPath = "";
    switch (lang) {
        case "java":
            uri = "file:./file.java"
            rootPath = "file:///D:/Projects/WebEditorServices/jsonrpc-ws-proxy/JAVA_PROJECT"
            break;
        case "cpp":
            uri = "file://./file.cpp"
            rootPath = "file:///D:/Projects/WebEditorServices/jsonrpc-ws-proxy/CPP_PROJECT"
            break;
        case "csharp":
            uri = "file://./file.cs"
            rootPath = "file:///D:/Projects/WebEditorServices/jsonrpc-ws-proxy/CSHARP_PROJECT"
            break;
    }
    ReplaceModel(lang, uri, rootPath)
}

function ReplaceModel(lang: string, uri: string, rootPath: string) {
    const oldModel = editor.getModel()
    let oldVal = "";
    if (oldModel) {
        oldVal = oldModel.getValue()
        oldModel.dispose()
    }
    editor.setModel(monaco.editor.createModel(oldVal, lang, monaco.Uri.parse(uri)))
    let props = {rootUri: rootPath}
    console.log(editor)
    console.log(props)
    //@ts-ignore
    MonacoServices.install(editor, props)
}

document.getElementById('langSelector')!.addEventListener('change', function () {
    // @ts-ignore
    let lang = this.value
    change_monaco_lang(lang as string)
})

change_monaco_lang('cpp')