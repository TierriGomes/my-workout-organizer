package com.tierriapps.myworkoutorganizer.feature_main.utils

enum class DivisionName(val char: Char) {
    A('A'),
    B('B'),
    C('C'),
    D('D'),
    E('E'),
    F('F'),
    G('G'),
    H('H'),
    I('I'),
    J('J'),
    K('K'),
    L('L'),
    M('M'),
    N('N'),
    O('O'),
    P('P'),
    Q('Q'),
    R('R'),
    S('S'),
    T('T'),
    U('U'),
    V('V'),
    W('W'),
    X('X'),
    Y('Y'),
    Z('Z');
}
fun getDivisionByChar(char: Char): DivisionName{
    return DivisionName.values().filter { it.char == char }[0]
}