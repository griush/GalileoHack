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

package com.google.location.custom_suplclient.asn1.supl2.lpp;

// Copyright 2008 Google Inc. All Rights Reserved.
/*
 * This class is AUTOMATICALLY GENERATED. Do NOT EDIT.
 */


//
//
import com.google.location.custom_suplclient.asn1.base.Asn1Enumerated;
import com.google.location.custom_suplclient.asn1.base.Asn1Object;
import com.google.location.custom_suplclient.asn1.base.Asn1Sequence;
import com.google.location.custom_suplclient.asn1.base.Asn1Tag;
import com.google.location.custom_suplclient.asn1.base.BitStream;
import com.google.location.custom_suplclient.asn1.base.BitStreamReader;
import com.google.location.custom_suplclient.asn1.base.SequenceComponent;
import com.google.common.collect.ImmutableList;
import java.util.Collection;
import javax.annotation.Nullable;


/**
* 
*/
public  class GNSS_ID extends Asn1Sequence {
  //

  private static final Asn1Tag TAG_GNSS_ID
      = Asn1Tag.fromClassAndNumber(-1, -1);

  public GNSS_ID() {
    super();
  }

  @Override
  @Nullable
  protected Asn1Tag getTag() {
    return TAG_GNSS_ID;
  }

  @Override
  protected boolean isTagImplicit() {
    return true;
  }

  public static Collection<Asn1Tag> getPossibleFirstTags() {
    if (TAG_GNSS_ID != null) {
      return ImmutableList.of(TAG_GNSS_ID);
    } else {
      return Asn1Sequence.getPossibleFirstTags();
    }
  }

  /**
   * Creates a new GNSS_ID from encoded stream.
   */
  public static GNSS_ID fromPerUnaligned(byte[] encodedBytes) {
    GNSS_ID result = new GNSS_ID();
    result.decodePerUnaligned(new BitStreamReader(encodedBytes));
    return result;
  }

  /**
   * Creates a new GNSS_ID from encoded stream.
   */
  public static GNSS_ID fromPerAligned(byte[] encodedBytes) {
    GNSS_ID result = new GNSS_ID();
    result.decodePerAligned(new BitStreamReader(encodedBytes));
    return result;
  }



  @Override protected boolean isExtensible() {
    return true;
  }

  @Override public boolean containsExtensionValues() {
    for (SequenceComponent extensionComponent : getExtensionComponents()) {
      if (extensionComponent.isExplicitlySet()) return true;
    }
    return false;
  }

  
  private GNSS_ID.gnss_idType gnss_id_;
  public GNSS_ID.gnss_idType getGnss_id() {
    return gnss_id_;
  }
  /**
   * @throws ClassCastException if value is not a GNSS_ID.gnss_idType
   */
  public void setGnss_id(Asn1Object value) {
    this.gnss_id_ = (GNSS_ID.gnss_idType) value;
  }
  public GNSS_ID.gnss_idType setGnss_idToNewInstance() {
    gnss_id_ = new GNSS_ID.gnss_idType();
    return gnss_id_;
  }
  

  

  

  @Override public Iterable<? extends SequenceComponent> getComponents() {
    ImmutableList.Builder<SequenceComponent> builder = ImmutableList.builder();
    
    builder.add(new SequenceComponent() {
          Asn1Tag tag = Asn1Tag.fromClassAndNumber(2, 0);

          @Override public boolean isExplicitlySet() {
            return getGnss_id() != null;
          }

          @Override public boolean hasDefaultValue() {
            return false;
          }

          @Override public boolean isOptional() {
            return false;
          }

          @Override public Asn1Object getComponentValue() {
            return getGnss_id();
          }

          @Override public void setToNewInstance() {
            setGnss_idToNewInstance();
          }

          @Override public Collection<Asn1Tag> getPossibleFirstTags() {
            return tag == null ? GNSS_ID.gnss_idType.getPossibleFirstTags() : ImmutableList.of(tag);
          }

          @Override
          public Asn1Tag getTag() {
            return tag;
          }

          @Override
          public boolean isImplicitTagging() {
            return true;
          }

          @Override public String toIndentedString(String indent) {
                return "gnss_id : "
                    + getGnss_id().toIndentedString(indent);
              }
        });
    
    return builder.build();
  }

  @Override public Iterable<? extends SequenceComponent>
                                                    getExtensionComponents() {
    ImmutableList.Builder<SequenceComponent> builder = ImmutableList.builder();
      
      return builder.build();
    }

  
  // Copyright 2008 Google Inc. All Rights Reserved.
/*
 * AUTOMATICALLY GENERATED. Do NOT EDIT.
 */


//

/**
 * 
 */
public static class gnss_idType extends Asn1Enumerated {
  public enum Value implements Asn1Enumerated.Value {
    gps(0),
    sbas(1),
    qzss(2),
    galileo(3),
    glonass(4),
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


  
  public void setTo_gps() {
    setValue(Value.gps);
  }
  
  public void setTo_sbas() {
    setValue(Value.sbas);
  }
  
  public void setTo_qzss() {
    setValue(Value.qzss);
  }
  
  public void setTo_galileo() {
    setValue(Value.galileo);
  }
  
  public void setTo_glonass() {
    setValue(Value.glonass);
  }
  


  public enum ExtensionValue implements Asn1Enumerated.Value {
    bds(5),
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

  
  public void setExtTo_bds() {
    setValue(ExtensionValue.bds);
  }
  

  

  private static final Asn1Tag TAG_gnss_idType
      = Asn1Tag.fromClassAndNumber(-1, -1);

  public gnss_idType() {
    super();
    // use template substitution instead of calling getDefaultValue(), since
    // calling virtual methods from a ctor is frowned upon here.
    setValue(null
);
  }

  @Override
  @Nullable
  protected Asn1Tag getTag() {
    return TAG_gnss_idType;
  }

  @Override
  protected boolean isTagImplicit() {
    return true;
  }

  public static Collection<Asn1Tag> getPossibleFirstTags() {
    if (TAG_gnss_idType != null) {
      return ImmutableList.of(TAG_gnss_idType);
    } else {
      return Asn1Enumerated.getPossibleFirstTags();
    }
  }

  @Override protected boolean isExtensible() {
    return true;
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
   * Creates a new gnss_idType from encoded stream.
   */
  public static gnss_idType fromPerUnaligned(byte[] encodedBytes) {
    gnss_idType result = new gnss_idType();
    result.decodePerUnaligned(new BitStreamReader(encodedBytes));
    return result;
  }

  /**
   * Creates a new gnss_idType from encoded stream.
   */
  public static gnss_idType fromPerAligned(byte[] encodedBytes) {
    gnss_idType result = new gnss_idType();
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
    return "gnss_idType = " + getValue() + ";\n";
  }
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
    StringBuilder builder = new StringBuilder();
    builder.append("GNSS_ID = {\n");
    final String internalIndent = indent + "  ";
    for (SequenceComponent component : getComponents()) {
      if (component.isExplicitlySet()) {
        builder.append(internalIndent)
            .append(component.toIndentedString(internalIndent));
      }
    }
    if (isExtensible()) {
      builder.append(internalIndent).append("...\n");
      for (SequenceComponent component : getExtensionComponents()) {
        if (component.isExplicitlySet()) {
          builder.append(internalIndent)
              .append(component.toIndentedString(internalIndent));
        }
      }
    }
    builder.append(indent).append("};\n");
    return builder.toString();
  }
}
