import React, { useState } from 'react';
import { Button} from 'react-bootstrap';
import './EmbeddingInstruction.css';

function EmbeddingInstruction() {
    const [firstParagraph] = useState(" Берем сначала укропу,\n" +
        "                потом кошачью жопу\n" +
        "                25 картошек,\n" +
        "                17 мондовошек,\n" +
        "                Ведро воды и хуй туды.\n" +
        "                Охапку дров и плов готов!");
    const [secondParagraph] = useState("<p>Lorem ipsum proin leo ligula ornare morbi justo sodales, integer gravida <a href=\"#\">donec porta massa</a>: urna elementum malesuada odio, commodo vivamus, leo diam metus elementum. Magna sem diam <i>at auctor, at nec</i> fusce orci rutrum orci arcu et ligula sed. Arcu <a href=\"#\">amet ultricies nec</a> gravida vivamus, molestie bibendum malesuada lorem sem elementum a diam mauris diam quam ut ligula. Tellus ornare urna, molestie maecenas sed non massa justo sodales sem urna nibh. Ut tempus <i>curabitur eros</i> <b>donec sodales</b> ut sapien, fusce vitae odio congue mattis at bibendum cursus urna, <a href=\"#\">proin at nec sapien quam</a> tellus massa.</p><p>Justo donec sapien porttitor sagittis malesuada morbi <a href=\"#\">at curabitur nibh, ligula vulputate.</a> Auctor morbi gravida ut eget pharetra ultricies, pellentesque arcu auctor sit diam pellentesque vivamus massa, ornare nulla elementum. Ipsum curabitur pharetra, odio porttitor donec eu cursus congue sodales quam adipiscing sagittis ut urna, auctor enim. Sagittis, pellentesque ut donec: amet ut diam sodales, magna bibendum adipiscing ultricies integer, cursus ut curabitur, eget nulla diam.</p><p>At eu molestie et metus at commodo elementum vitae curabitur sem pharetra sit ut rutrum fusce mattis quisque congue&nbsp;&mdash; elementum rutrum morbi pellentesque. <a href=\"#\">Quam et pellentesque eros eu</a> <b>et vitae arcu</b> eros, eu auctor risus malesuada eros congue. Eu tempus pharetra proin porta tellus mauris ligula porta nec enim. Non malesuada sit, risus lectus orci urna non gravida justo, vitae <a href=\"#\">cursus vivamus cursus</a> sem. Sit orci sit morbi tellus sagittis sit, fusce: vitae ligula, integer.</p><p>Commodo sodales sit nulla, rutrum ornare malesuada justo ligula porta malesuada magna vivamus enim eu commodo congue eros. Leo risus&nbsp;&mdash; eu magna duis fusce arcu proin <i>morbi, duis</i> proin porta duis ligula. Ornare, vulputate nibh massa, rutrum mauris, cursus ornare, congue porta ipsum ligula lectus leo arcu lorem. Eget&nbsp;&mdash; nec porta magna quam gravida, vulputate justo congue quisque&nbsp;&mdash; vivamus porttitor in urna porttitor&nbsp;&mdash; nec.</p><p>Amet vivamus auctor duis, metus ligula commodo, sit amet congue. Duis, tellus, pharetra et tempus metus maecenas quisque et sagittis, elementum eget vitae elementum eros: odio pharetra cursus. Vitae sapien sed odio vivamus quisque pellentesque proin <b>elementum enim</b> <i>sodales. Integer odio metus gravida ornare, odio integer fusce nec</i> rutrum eros donec lectus orci pellentesque porta, <i>nam duis</i> lorem elementum sed ornare pellentesque eget.</p> ");

    const copyText = () => {
        navigator.clipboard.writeText(secondParagraph)
            .then(() => {
                console.log('Text copied to clipboard');
            })
            .catch(err => {
                console.error('Error copying text:', err);
            });
    };

    return (
        <div className="instruction-page">
            <div className="chat-header">
                YagaTalk
            </div>
            <div className="paragraph">
                {firstParagraph}
            </div>
            <div className="paragraph">
                {secondParagraph}
            </div>
            <Button onClick={copyText} variant="primary">
                Copy
            </Button>
        </div>
    );
}

export default EmbeddingInstruction;