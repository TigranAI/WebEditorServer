require.config({ paths: { vs: 'editor/node_modules/monaco-editor/min/vs' } });

require(['vs/editor/editor.main'], function () {
    window.editorInstance = monaco.editor.create(document.getElementById("container"), {
        model: monaco.editor.createModel("", 'cpp', monaco.Uri.parse('inmemory:file.txt')),
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
    })
});

window.onresize = function () {
    window.editorInstance.layout()
}

function change_monaco_lang(lang) {
    const oldModel = window.editorInstance.getModel()
    if (oldModel) oldModel.dispose()
    window.editorInstance.setModel(monaco.editor.createModel("", lang, monaco.Uri.parse('inmemory:file.txt')))
}

document.getElementById('langSelector').addEventListener('change', function () {
    let lang = this.value
    change_monaco_lang(lang)
})