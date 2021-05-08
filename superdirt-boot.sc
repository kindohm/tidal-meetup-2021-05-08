(
s.options.numBuffers = 1024 * 16;
s.options.memSize = 8192 * 16;
s.options.maxNodes = 1024 * 64;
s.options.numOutputBusChannels = 2;
s.options.numInputBusChannels = 0;

s.waitForBoot {
	~dirt = SuperDirt(2, s);

	s.sync;
	~dirt.start(57120, [0]);

	MIDIClient.init;

	~rytmOut = MIDIOut.newByName("Elektron Analog Rytm", "Elektron Analog Rytm");
	~rytmOut.latency = 0;
	~dirt.soundLibrary.addMIDI(\rytm, ~rytmOut);
};
s.latency = 0;
);

// Eval this block to enable MIDI input.
(
var on, off, cc;
var osc;

// Tidal OSC target
osc = NetAddr.new("127.0.0.1", 6010);

MIDIClient.init;

// connect only to specific device on my system
MIDIIn.connect(inport: 0, device: 2);

// you can optionally uncomment this to enable MIDI
// input from all devices on your system:
//MIDIIn.connectAll;

// wire up MIDI CC input to forward to Tidal as OSC
cc = MIDIFunc.cc({ |val, num, chan, src|
	osc.sendMsg("/ctrl", num.asString, val/127);
});

if (~stopMidiToOsc != nil, {
	~stopMidiToOsc.value;
});

~stopMidiToOsc = {
	on.free;
	off.free;
	cc.free;
};
)

