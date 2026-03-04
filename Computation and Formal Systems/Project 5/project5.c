
#include <stdio.h>
#include <stdlib.h>
#include <math.h>
#include "Circuit.h"

static Circuit nor_Circuit() {

    char *title = "Two NOT gates connected by an AND gate to make a NOR circuit";

    // Create the inputs
    int NINPUTS = 2;
    Boolean x = new_Boolean(false);
    Boolean y = new_Boolean(false);
    Boolean* inputs = new_Boolean_array(NINPUTS);
    inputs[0] = x;
    inputs[1] = y;

    // Create the outputs
    int NOUTPUTS = 1;
    Boolean result = new_Boolean(false);
    Boolean* outputs = new_Boolean_array(NOUTPUTS);
    outputs[0] = result;

    // Create the gates
    int NGATES = 3;
    Gate N1 = new_Inverter();
    Gate N2 = new_Inverter();
    Gate A = new_AndGate();
    Gate* gates = new_Gate_array(NGATES);
    gates[0] = N1;
    gates[1] = N2;
    gates[2] = A;

    // Create the circuit
    Circuit circuit = new_Circuit(title,
				  NINPUTS, inputs,
				  NOUTPUTS, outputs,
				  NGATES, gates);

    // Connect the gates in the circuit
    Circuit_connect(circuit, x, Gate_getInput(N1, 0));
    Boolean not_x = Gate_getOutput(N1);

    Circuit_connect(circuit, y, Gate_getInput(N2, 0));
    Boolean not_y = Gate_getOutput(N2);

    Circuit_connect(circuit, not_x, Gate_getInput(A, 0));
    Circuit_connect(circuit, not_y, Gate_getInput(A, 1));
    Boolean x_nor_y = Gate_getOutput(A);

    Circuit_connect(circuit, x_nor_y, result);

    // Done!
    return circuit;
}
static Circuit and_Circuit() {

    char *title = "Two NAND gates, each with the same two inputs, connected by another NAND gate to make an AND circuit";

    // Create the inputs
    int NINPUTS = 2;
    Boolean x = new_Boolean(false);
    Boolean y = new_Boolean(false);
    Boolean* inputs = new_Boolean_array(NINPUTS);
    inputs[0] = x;
    inputs[1] = y;

    // Create the outputs
    int NOUTPUTS = 1;
    Boolean result = new_Boolean(false);
    Boolean* outputs = new_Boolean_array(NOUTPUTS);
    outputs[0] = result;

    // Create the gates
    int NGATES = 6;
    Gate A1 = new_AndGate();
    Gate A2 = new_AndGate();
    Gate N1 = new_Inverter();
    Gate N2 = new_Inverter();
    Gate A3 = new_AndGate();
    Gate N3 = new_Inverter();
    Gate* gates = new_Gate_array(NGATES);
    gates[0] = A1;
    gates[1] = A2;
    gates[2] = N1;
    gates[3] = N2;
    gates[4] = A3;
    gates[5] = N3;

    // Create the circuit
    Circuit circuit = new_Circuit(title,
				  NINPUTS, inputs,
				  NOUTPUTS, outputs,
				  NGATES, gates);

    // Connect the gates in the circuit
    Circuit_connect(circuit, x, Gate_getInput(A1, 0));
    Circuit_connect(circuit, y, Gate_getInput(A1, 1));
    Boolean x_and_y1 = Gate_getOutput(A1);

    Circuit_connect(circuit, x, Gate_getInput(A2, 0));
    Circuit_connect(circuit, y, Gate_getInput(A2, 1));
    Boolean x_and_y2 = Gate_getOutput(A2);

    Circuit_connect(circuit, x_and_y1, Gate_getInput(N1, 0));
    Boolean x_nand_y1 = Gate_getOutput(N1);
    
    Circuit_connect(circuit, x_and_y2, Gate_getInput(N2, 0));
    Boolean x_nand_y2 = Gate_getOutput(N2);

    Circuit_connect(circuit, x_nand_y1, Gate_getInput(A3, 0));
    Circuit_connect(circuit, x_nand_y2, Gate_getInput(A3, 1));
    Boolean x_nor_y = Gate_getOutput(A3);

    Circuit_connect(circuit, x_nor_y, Gate_getInput(N3, 0));
    Boolean x_or_y = Gate_getOutput(N3);

    Circuit_connect(circuit, x_or_y, result);

    // Done!
    return circuit;
}
static Circuit x_and_y_or_x_or_y_and_not_z_Circuit() {

    char *title = "An AND gate connected by an OR gate to an AND gate connecting an OR gate (all taking the same two inputs) and a NOT gate (taking a third input)";

    // Create the inputs
    int NINPUTS = 3;
    Boolean x = new_Boolean(false);
    Boolean y = new_Boolean(false);
    Boolean z = new_Boolean(false);
    Boolean* inputs = new_Boolean_array(NINPUTS);
    inputs[0] = x;
    inputs[1] = y;
    inputs[2] = z;

    // Create the outputs
    int NOUTPUTS = 1;
    Boolean result = new_Boolean(false);
    Boolean* outputs = new_Boolean_array(NOUTPUTS);
    outputs[0] = result;

    // Create the gates
    int NGATES = 5;
    Gate A1 = new_AndGate();
    Gate O1 = new_OrGate();
    Gate N = new_Inverter();
    Gate A2 = new_AndGate();
    Gate O2 = new_OrGate();
    Gate* gates = new_Gate_array(NGATES);
    gates[0] = A1;
    gates[1] = O1;
    gates[2] = N;
    gates[3] = A2;
    gates[4] = O2;

    // Create the circuit
    Circuit circuit = new_Circuit(title,
				  NINPUTS, inputs,
				  NOUTPUTS, outputs,
				  NGATES, gates);

    // Connect the gates in the circuit
    Circuit_connect(circuit, x, Gate_getInput(A1, 0));
    Circuit_connect(circuit, y, Gate_getInput(A1, 1));
    Boolean x_and_y = Gate_getOutput(A1);

    Circuit_connect(circuit, x, Gate_getInput(O1, 0));
    Circuit_connect(circuit, y, Gate_getInput(O1, 1));
    Boolean x_or_y = Gate_getOutput(O1);

    Circuit_connect(circuit, z, Gate_getInput(N, 0));
    Boolean not_z = Gate_getOutput(N);

    Circuit_connect(circuit, x_or_y, Gate_getInput(A2, 0));
    Circuit_connect(circuit, not_z, Gate_getInput(A2, 1));
    Boolean x_or_y_and_not_z = Gate_getOutput(A2);

    Circuit_connect(circuit, x_and_y, Gate_getInput(O2, 0));
    Circuit_connect(circuit, x_or_y_and_not_z, Gate_getInput(O2, 1));
    Boolean x_and_y_or_x_or_y_and_not_z = Gate_getOutput(O2);

    Circuit_connect(circuit, x_and_y_or_x_or_y_and_not_z, result);

    // Done!
    return circuit;
}



// static void test3In1Out(Circuit circuit, bool in0, bool in1, bool in2) {
//     Circuit_setInput(circuit, 0, in0);
//     Circuit_setInput(circuit, 1, in1);
//     Circuit_setInput(circuit, 2, in2);
//     Circuit_update(circuit);
//     printf("%s %s %s -> %s\n",
// 	   Boolean_toString(Circuit_getInput(circuit, 0)),
// 	   Boolean_toString(Circuit_getInput(circuit, 1)),
// 	   Boolean_toString(Circuit_getInput(circuit, 2)),
// 	   Boolean_toString(Circuit_getOutput(circuit, 0)));
// }

static void testCircuit(Circuit circuit)
{
    for (int i=0; i<pow(2, Circuit_numInputs(circuit)); i++)
    {
        printf("\n");
        int grant = i;
        int digit;
        int zhoop = Circuit_numInputs(circuit) - 1;
        while (zhoop >= 0)
        {
            digit = grant % 2;
            if (digit == 1)
                Circuit_setInput(circuit, zhoop, true);
            else
                Circuit_setInput(circuit, zhoop, false);
            grant = grant/2;
            zhoop--;
        }
        Circuit_update(circuit);
        for (int j=0; j<Circuit_numInputs(circuit); j++)
        {
            printf("%s ", Boolean_toString(Circuit_getInput(circuit, j)));
        }
        if (Circuit_numOutputs(circuit) == 1)
            printf("-> %s\n", Boolean_toString(Circuit_getOutput(circuit, 0)));
        else
        {
            printf("-> ");
            for (int k=0; k<Circuit_numOutputs(circuit); k++)
            {
                printf("%s ", Boolean_toString(Circuit_getOutput(circuit, k)));
            }
        }
    }
}

int main(int argc, char **argv)
{
    // Circuit circuit = or_Circuit();
    // Circuit_dump(circuit);
    // printf("\n");
    // printf("Testing: Some input(s) false: should be false\n");
    // test3In1Out(circuit, true, true, false);
    // printf("Testing: All inputs true: should be true\n");
    // test3In1Out(circuit, true, true, true);
    // printf("\nNote: Your program needs a function that tests ANY circuit on ALL possible\ncombinations of input values, in order from all false to all true, per the\nproject description.\n");
    // free_Circuit(circuit);

    Circuit circuit1 = nor_Circuit();
    Circuit circuit2 = and_Circuit();
    Circuit circuit3 = x_and_y_or_x_or_y_and_not_z_Circuit();
    printf("\n%s", Circuit_getTitle(circuit1));
    testCircuit(circuit1);
    printf("\n\n%s", Circuit_getTitle(circuit2));
    testCircuit(circuit2);
    printf("\n\n%s", Circuit_getTitle(circuit3));
    testCircuit(circuit3);
}
