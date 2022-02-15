var NEW_LINE_EXP = /\n(?!$)/g;

function renderDoc() {
    var renderer = new marked.Renderer();

    renderer.code = function (code, lang) {
        var match = code.match(NEW_LINE_EXP);
        var linesNum = match ? match.length + 1 : 1;

        var lines = new Array(linesNum + 1).join('<span></span>');
        let html;
        try {
            html = Prism.highlight(code, Prism.languages[lang], lang);
        } catch (e) {
            debugger
        }
        const lineNumbersHtml = `<span class="line-numbers-rows">${lines}</span>`
        return `<pre class="line-numbers language-${lang}"><code class="line-numbers language-${lang}">${html}${lineNumbersHtml}</code></pre>`;
    };
    marked.setOptions({
        renderer: renderer,
        langPrefix: 'line-numbers language-',
    });

    const fileName = document.location.search.substring(1) || 'nani-doc'

    fetch(`../doc/${fileName}.md`, {
        method: "GET",
        mode: "cors",
        credentials: "include",
    }).then((res) => {
        res.text().then((text) => {
            document.getElementById('content').innerHTML =
                marked.parse(text);
        })
    })
}
renderDoc();