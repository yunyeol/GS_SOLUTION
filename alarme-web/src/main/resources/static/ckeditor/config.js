/**
 * @license Copyright (c) 2003-2018, CKSource - Frederico Knabben. All rights reserved.
 * For licensing, see https://ckeditor.com/legal/ckeditor-oss-license
 */

CKEDITOR.editorConfig = function( config ) {
	// Define changes to default configuration here. For example:
	// config.language = 'fr';
	// config.uiColor = '#AADC6E';

    config.toolbar = [
        { name: 'document', items: [ 'Source' ] },
        { name: 'styles', items: [ 'Format', 'Font', 'FontSize' ] },
        { name: 'basicstyles', items: [ 'Bold', 'Italic', 'Underline', 'Strike'] },
        { name: 'colors', items: [ 'TextColor', 'BGColor' ] },
        { name: 'align', items: [ 'JustifyLeft', 'JustifyCenter', 'JustifyRight', 'JustifyBlock' ] },
        { name: 'links', items: [ 'Link', 'Unlink' ] },
        { name: 'insert', items: [ 'Image', 'Table' ] },
        { name: 'Mapping', items:['LoginId', 'UserName', 'Title', 'Contents']}
    ],
    config.customConfig = '',
    config.height = 400
    config.resize = 0
};
