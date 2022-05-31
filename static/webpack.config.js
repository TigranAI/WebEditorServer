const path = require('path');
const lib = path.resolve(__dirname, "editor/scripts");
const TenserPlugin = require('terser-webpack-plugin');

module.exports = {
    entry: {
        "main": path.resolve(lib, "main.js"),
        "editor.worker": 'monaco-editor/esm/vs/editor/editor.worker.js'
    },
    plugins: [
        new TenserPlugin()
    ],
    output: {
        publicPath: "/editor/scripts/",
        filename: '[name].bundle.js',
        path: lib
    },
    module: {
        rules: [{
            test: /\.css$/,
            use: [
                "style-loader",
                "css-loader",
            ],
        },]
    },
    target: 'web',
    resolve: {
        alias: {
            'vscode': require.resolve('monaco-languageclient/lib/vscode-compatibility')
        },
        extensions: ['.js', '.json'],
        fallback: {
            fs: false,
            child_process: false,
            net: false,
            crypto: false,
            url: require.resolve('url'),
            path: require.resolve('path'),
            os: require.resolve('os-browserify'),
            buffer: require.resolve('buffer')
        }
    }
};
