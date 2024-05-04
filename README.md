# GalileoHack

### Questions
- How many Galileo satellites are used on your phone?
- How is Galileo contributing to your position accuracy?
- Are the signals being altered?

### Answers
- Count total sats visible, see which ones are Galileo (constellationtype = 6)
- Measure local error, filter by Galileo, compare to other constellations
- Raw measurements, check how many frames dropped for checksum error and relation with SNR, suposedly, the more the dBm the less frames are dropped

### Data provided by GnssMeasurement android API structure
```
GnssMeasurement:
  Svid                          = 24                    # sat id
  ConstellationType             = 6                     # indicates GLONASS, GPS, Galileo etc
  TimeOffsetNanos               = 0.0                   # elapsed time since message reception
  State                         = CodeLock|BitSync|SubframeSync|TowDecoded|TowKnown|GalE1bcCodeLock|E1c2ndCodeLock|GalE1bPageSync|2ndCodeLock|Other(10100000000000000)
                                                        # this indicates the status of the hardware decoder, PLL, clock recovery, frame sync, etc
  ReceivedSvTimeNanos           = 513828348107144       # atomic time of sat when the msg was sent
  ReceivedSvTimeUncertaintyNanos           = 53         # quantum error of it?
  Cn0DbHz                       = 19.6                  # SNR basically
  BasebandCn0DbHz               = 13.600000000000001    # another basically SNR aswell
  PseudorangeRateMetersPerSecond = -509.4541931152344   # pseudorange at measurement, could be said its the radial speed of the sat in relation to us
  PseudorangeRateUncertaintyMetersPerSecond = 0.8104999661445618  # error of last thing, quantum source?
  AccumulatedDeltaRangeState    = HalfCycleReported     # idk
  AccumulatedDeltaRangeMeters   = 0.0                   # idfk
  AccumulatedDeltaRangeUncertaintyMeters   = 0.0        # error of idfk
  CarrierFrequencyHz            = 1.57542003E9          # self explanatory, RF carrier freq of C/A
  MultipathIndicator            = Unknown               # multipath detection apparently, cool
  AgcLevelDb                    = 1.0                   # preamp gain (AGC), usually when high indicates compensation for low SNR
  CodeType                      =                       # code type???? idfk its not even on google documentation
```

### Pseudo range calculation
```
tTx = ReceivedSvTimeNanos + TimeOffsetNanos
tRx_GNSS = TimeNanos + timeOffsetNanos - (fullInterSignalBiasNanos + satelliteInterSignalBiasNanos)
tRx = tRx_GNSS % 10e8
rho = (tRx - tTx)/1e9 * c
```
