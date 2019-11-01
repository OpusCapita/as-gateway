const path = require('path');
const webpack = require('webpack');

module.exports = {
    entry: {
        app: ['babel-polyfill', './src/main/client/index.js']
    },
    devtool: 'eval-source-map',
    cache: true,
    output: {
        path: path.resolve(__dirname, './src/main/resources/static'),
        publicPath: '/',
        filename: 'built/bundle-[name].js',
        library: 'as-gateway-[name]',
        libraryTarget: 'umd',
        umdNamedDefine: true
    },

    //exclude empty dependencies, require for Joi
    node: {
        net: 'empty',
        tls: 'empty',
        dns: 'empty'
    },

    bail: true,

    plugins: [
        new webpack.ContextReplacementPlugin(/moment[\/\\]locale$/, /en|de/),
        new webpack.NoEmitOnErrorsPlugin(),
        new webpack.DefinePlugin({
            'process.env.NODE_ENV': '"production"'
        }),
        new webpack.optimize.OccurrenceOrderPlugin(true),
    ],

    resolve: {
        modules: ['NODE_PATH', 'node_modules'],
        extensions: ['.js']
    },

    resolveLoader: {
        modules: ['NODE_PATH', 'node_modules'],
        extensions: ['.js']
    },

    module: {
        rules: [
            {
                test: /.jsx?$/,
                loader: 'babel-loader',
                include: [
                    path.join(__dirname, 'src/main/client')
                ],
                options: {
                    compact: true,
                    presets: [
                        '@babel/preset-env',
                        '@babel/preset-react'
                    ],
                    plugins: [
                        '@babel/plugin-proposal-class-properties'
                    ]
                }
            }
        ]
    }
};