require('monaco-editor');
(self as any).MonacoEnvironment = {
    getWorkerUrl: () => '/editor/scripts/editor.worker.bundle.js'
}
require('./index');