// Copyright 2018 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//      http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.location.custom_suplclient.asn1.supl2.ver2_ulp_components;

// Copyright 2008 Google Inc. All Rights Reserved.
/*
 * AUTOMATICALLY GENERATED. Do NOT EDIT.
 */


//
//
import com.google.location.custom_suplclient.asn1.base.Asn1Enumerated;
import com.google.location.custom_suplclient.asn1.base.Asn1Tag;
import com.google.location.custom_suplclient.asn1.base.BitStream;
import com.google.location.custom_suplclient.asn1.base.BitStreamReader;
import com.google.common.collect.ImmutableList;
import java.util.Collection;
import javax.annotation.Nullable;


/**
 * 
 */
public  class UTRANGANSSDriftRate extends Asn1Enumerated {
  public enum Value implements Asn1Enumerated.Value {
    utran_GANSSDrift0(1),
    utran_GANSSDrift1(2),
    utran_GANSSDrift2(3),
    utran_GANSSDrift5(4),
    utran_GANSSDrift10(5),
    utran_GANSSDrift15(6),
    utran_GANSSDrift25(7),
    utran_GANSSDrift50(8),
    utran_GANSSDrift_1(9),
    utran_GANSSDrift_2(10),
    utran_GANSSDrift_5(11),
    utran_GANSSDrift_10(12),
    utran_GANSSDrift_15(13),
    utran_GANSSDrift_25(14),
    utran_GANSSDrift_50(15),
    ;

    Value(int i) {
      value = i;
    }

    private int value;
    public int getAssignedValue() {
      return value;
    }

    @Override public boolean isExtensionValue() {
      return false;
    }
  }

  @Override
  protected Value getDefaultValue() {
    return null
;
  }

  @SuppressWarnings("unchecked")
  public Value enumValue() {
    return (Value) getValue();
  }


  
  public void setTo_utran_GANSSDrift0() {
    setValue(Value.utran_GANSSDrift0);
  }
  
  public void setTo_utran_GANSSDrift1() {
    setValue(Value.utran_GANSSDrift1);
  }
  
  public void setTo_utran_GANSSDrift2() {
    setValue(Value.utran_GANSSDrift2);
  }
  
  public void setTo_utran_GANSSDrift5() {
    setValue(Value.utran_GANSSDrift5);
  }
  
  public void setTo_utran_GANSSDrift10() {
    setValue(Value.utran_GANSSDrift10);
  }
  
  public void setTo_utran_GANSSDrift15() {
    setValue(Value.utran_GANSSDrift15);
  }
  
  public void setTo_utran_GANSSDrift25() {
    setValue(Value.utran_GANSSDrift25);
  }
  
  public void setTo_utran_GANSSDrift50() {
    setValue(Value.utran_GANSSDrift50);
  }
  
  public void setTo_utran_GANSSDrift_1() {
    setValue(Value.utran_GANSSDrift_1);
  }
  
  public void setTo_utran_GANSSDrift_2() {
    setValue(Value.utran_GANSSDrift_2);
  }
  
  public void setTo_utran_GANSSDrift_5() {
    setValue(Value.utran_GANSSDrift_5);
  }
  
  public void setTo_utran_GANSSDrift_10() {
    setValue(Value.utran_GANSSDrift_10);
  }
  
  public void setTo_utran_GANSSDrift_15() {
    setValue(Value.utran_GANSSDrift_15);
  }
  
  public void setTo_utran_GANSSDrift_25() {
    setValue(Value.utran_GANSSDrift_25);
  }
  
  public void setTo_utran_GANSSDrift_50() {
    setValue(Value.utran_GANSSDrift_50);
  }
  


  public enum ExtensionValue implements Asn1Enumerated.Value {
    ;

    ExtensionValue(int i) {
      value = i;
    }

    private int value;
    @Override public int getAssignedValue() {
      return value;
    }

    @Override public boolean isExtensionValue() {
      return true;
    }
  }

  @SuppressWarnings("unchecked")
  public ExtensionValue extEnumValue() {
    return (ExtensionValue) getValue();
  }

  

  

  private static final Asn1Tag TAG_UTRANGANSSDriftRate
      = Asn1Tag.fromClassAndNumber(-1, -1);

  public UTRANGANSSDriftRate() {
    super();
    // use template substitution instead of calling getDefaultValue(), since
    // calling virtual methods from a ctor is frowned upon here.
    setValue(null
);
  }

  @Override
  @Nullable
  protected Asn1Tag getTag() {
    return TAG_UTRANGANSSDriftRate;
  }

  @Override
  protected boolean isTagImplicit() {
    return true;
  }

  public static Collection<Asn1Tag> getPossibleFirstTags() {
    if (TAG_UTRANGANSSDriftRate != null) {
      return ImmutableList.of(TAG_UTRANGANSSDriftRate);
    } else {
      return Asn1Enumerated.getPossibleFirstTags();
    }
  }

  @Override protected boolean isExtensible() {
    return false;
  }

  @Override protected Asn1Enumerated.Value lookupValue(int ordinal) {
    return Value.values()[ordinal];
  }

  @Override protected Asn1Enumerated.Value lookupExtensionValue(int ordinal) {
    return ExtensionValue.values()[ordinal];
  }

  @Override protected int getValueCount() {
    return Value.values().length;
  }

  /**
   * Creates a new UTRANGANSSDriftRate from encoded stream.
   */
  public static UTRANGANSSDriftRate fromPerUnaligned(byte[] encodedBytes) {
    UTRANGANSSDriftRate result = new UTRANGANSSDriftRate();
    result.decodePerUnaligned(new BitStreamReader(encodedBytes));
    return result;
  }

  /**
   * Creates a new UTRANGANSSDriftRate from encoded stream.
   */
  public static UTRANGANSSDriftRate fromPerAligned(byte[] encodedBytes) {
    UTRANGANSSDriftRate result = new UTRANGANSSDriftRate();
    result.decodePerAligned(new BitStreamReader(encodedBytes));
    return result;
  }

  @Override public Iterable<BitStream> encodePerUnaligned() {
    return super.encodePerUnaligned();
  }

  @Override public Iterable<BitStream> encodePerAligned() {
    return super.encodePerAligned();
  }

  @Override public void decodePerUnaligned(BitStreamReader reader) {
    super.decodePerUnaligned(reader);
  }

  @Override public void decodePerAligned(BitStreamReader reader) {
    super.decodePerAligned(reader);
  }

  @Override public String toString() {
    return toIndentedString("");
  }

  public String toIndentedString(String indent) {
    return "UTRANGANSSDriftRate = " + getValue() + ";\n";
  }
}
