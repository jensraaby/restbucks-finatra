module.exports = {
    entry: "./main.js",

    output: {
        path: "../src/main/resources/js/",
        filename: "bundle.js"
    },
    module: {
        loaders: [
            { test: /\.css$/, loader: "style!css" }
        ]
    },
    devServer: {
        inline: true,
        port: 3333
    }
};
