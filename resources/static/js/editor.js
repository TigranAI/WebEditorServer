require.config({ paths: { vs: 'node_modules/monaco-editor/min/vs' } });

$(require(['vs/editor/editor.main'], function (){
    let element = document.getElementById("editor")
    const monacoEditor = monaco.editor.create(element, {
        language: "cpp"
    })
    window.onresize = function (){
        monacoEditor.layout()
    }
}))