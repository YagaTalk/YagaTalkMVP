const { override, getBabelLoader, useBabelRc, addWebpackPlugin } = require('customize-cra');
const HtmlWebpackPlugin = require('html-webpack-plugin');

module.exports = override(
    useBabelRc(),
    addWebpackPlugin(new HtmlWebpackPlugin({ inject: true, template: './public/index.html' })),
    (config) => {
        config.output.filename = 'static/js/main.js';
        config.plugins[5].options.filename = 'static/css/main.css';
        config.plugins[5].options.moduleFilename = () => 'static/css/main.css';
        return config;
    }
);