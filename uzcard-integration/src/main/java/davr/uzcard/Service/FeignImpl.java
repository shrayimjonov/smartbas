package davr.uzcard.Service;

public interface FeignImpl {

    <E> Object svgateConnect(E value);

    <E> Object svgateHistory(E value);

    <E> Object svgateTransaction(E value);

    <E> Object svgateBinList(E value);

    <E> Object genesis(E value);

    <E> Object boGateConnect(E value);

    <E> Object svgateEightConnect(E value);


}
